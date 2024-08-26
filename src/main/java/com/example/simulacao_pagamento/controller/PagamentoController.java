package com.example.simulacao_pagamento.controller;

import com.example.simulacao_pagamento.dto.CupomDescontoApplyDTO;
import com.example.simulacao_pagamento.dto.PagamentoDTO;
import com.example.simulacao_pagamento.dto.PagamentoProcessamentoDTO;
import com.example.simulacao_pagamento.exception.CustomException;
import com.example.simulacao_pagamento.model.CupomDesconto;
import com.example.simulacao_pagamento.model.ItemCarrinho;
import com.example.simulacao_pagamento.model.Pagamento;
import com.example.simulacao_pagamento.model.Usuario;
import com.example.simulacao_pagamento.service.CarrinhoService;
import com.example.simulacao_pagamento.service.CupomDescontoService;
import com.example.simulacao_pagamento.service.PagamentoService;
import com.example.simulacao_pagamento.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pagamento")
@Tag(name = "Pagamento", description = "APIs relacionadas ao processamento de pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;
    private final CarrinhoService carrinhoService;
    private final UsuarioService usuarioService;
    private final CupomDescontoService cupomDescontoService;
    private final RestTemplate restTemplate;

    @Autowired
    public PagamentoController(PagamentoService pagamentoService, CarrinhoService carrinhoService, UsuarioService usuarioService, CupomDescontoService cupomDescontoService, RestTemplate restTemplate) {
        this.pagamentoService = pagamentoService;
        this.carrinhoService = carrinhoService;
        this.usuarioService = usuarioService;
        this.cupomDescontoService = cupomDescontoService;
        this.restTemplate = restTemplate;
    }

    @Operation(summary = "Processar um pagamento", description = "Processa o pagamento dos itens no carrinho de um usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento processado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno ao processar o pagamento",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/processar")
    public ResponseEntity<String> processarPagamento(@Validated @RequestBody PagamentoProcessamentoDTO pagamentoProcessamentoDTO) {
        try {
            // Verificar se o usuário existe
            Usuario usuario = usuarioService.obterUsuarioPorId(pagamentoProcessamentoDTO.getUsuarioId())
                    .orElseThrow(() -> new CustomException("Usuário não encontrado."));

            // Recuperar itens do carrinho do usuário
            List<ItemCarrinho> itensCarrinho = carrinhoService.listarItensDoUsuario(usuario.getId());
            if (itensCarrinho.isEmpty()) {
                throw new CustomException("O carrinho do usuário está vazio.");
            }

            // Calcular o valor total dos itens no carrinho
            double valorTotal = itensCarrinho.stream()
                    .mapToDouble(ItemCarrinho::getValorTotal)
                    .sum();

            String cupomCodigo = pagamentoProcessamentoDTO.getCupomDesconto();
            double valorDesconto = 0;
            String cupomDescontoAplicado = null;

            if (cupomCodigo != null && !cupomCodigo.isEmpty()) {
                try {
                    // Criar a instância de CupomDescontoApplyDTO usando o setter
                    CupomDescontoApplyDTO cupomDescontoApplyDTO = new CupomDescontoApplyDTO();
                    cupomDescontoApplyDTO.setCodigo(cupomCodigo);

                    // Chama o endpoint /cupom/aplicar
                    ResponseEntity<String> response = restTemplate.postForEntity(
                            "http://localhost:8080/cupom/aplicar",
                            new HttpEntity<>(cupomDescontoApplyDTO),
                            String.class
                    );

                    if (response.getStatusCode() == HttpStatus.OK) {
                        // Extraia o valor do desconto da resposta e aplique-o ao total
                        String resposta = response.getBody();
                        double porcentagemDesconto = extrairPercentualDesconto(resposta);
                        valorDesconto = valorTotal * (porcentagemDesconto / 100);
                        valorTotal -= valorDesconto;
                        cupomDescontoAplicado = cupomCodigo;
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao aplicar o cupom: " + response.getBody());
                    }
                } catch (HttpClientErrorException e) {
                    return ResponseEntity.status(e.getStatusCode()).body("Erro ao aplicar o cupom de desconto: " + e.getResponseBodyAsString());
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro inesperado ao aplicar o cupom: " + e.getMessage());
                }
            }
            // Criar o pagamento
            Pagamento pagamento = new Pagamento();
            pagamento.setNumeroCartao(pagamentoProcessamentoDTO.getNumeroCartao());
            pagamento.setValidadeCartao(pagamentoProcessamentoDTO.getValidadeCartao());
            pagamento.setValorPago(valorTotal);
            pagamento.setValorDescontado(valorDesconto);
            pagamento.setCupomDescontoAplicado(cupomDescontoAplicado);
            pagamento.setUsuario(usuario);

            // Associar itens do carrinho ao pagamento
            for (ItemCarrinho item : itensCarrinho) {
                item.setPagamento(pagamento);
            }

            // Processar e salvar o pagamento com os itens associados
            pagamento.setItensCarrinho(itensCarrinho);
            pagamentoService.processarPagamento(pagamento);

            // Remover itens do carrinho
            for (ItemCarrinho item : itensCarrinho) {
                carrinhoService.removerItem(item.getId());
            }

            String mensagemSucesso = "Compra concluída com sucesso! Valor total após desconto: R$ " + String.format("%.2f", valorTotal)
                    + ". Cupom utilizado: " + cupomDescontoAplicado + ". Valor descontado: R$ " + String.format("%.2f", valorDesconto);
            return ResponseEntity.ok(mensagemSucesso);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro inesperado ao processar o pagamento: " + e.getMessage());
        }
    }

    // Método para extrair o percentual de desconto da resposta
    private double extrairPercentualDesconto(String resposta) {
        try {
            // Supondo que a resposta tenha o formato "Cupom aplicado com sucesso! Desconto de X%."
            String[] partes = resposta.split("Desconto de ");
            if (partes.length > 1) {
                String porcentagem = partes[1].replace("%.", "");
                return Double.parseDouble(porcentagem);
            }
        } catch (Exception e) {
            // Logar o erro e retornar 0 caso não seja possível extrair o percentual
            System.err.println("Erro ao extrair o percentual de desconto: " + e.getMessage());
        }
        return 0;
    }

    @Operation(summary = "Listar todos os pagamentos de um usuário", description = "Recupera todos os pagamentos já efetuados por um usuário específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamentos listados com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno ao listar os pagamentos",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/usuario/{usuarioId}/pagamentos")
    public ResponseEntity<List<PagamentoDTO>> listarPagamentosPorUsuario(@PathVariable Long usuarioId) {
        try {
            Usuario usuario = usuarioService.obterUsuarioPorId(usuarioId)
                    .orElseThrow(() -> new CustomException("Usuário não encontrado."));

            List<Pagamento> pagamentos = pagamentoService.listarPagamentosPorUsuario(usuario);
            List<PagamentoDTO> pagamentoDTOs = pagamentos.stream()
                    .map(PagamentoDTO::fromEntity)
                    .collect(Collectors.toList());

            if (pagamentoDTOs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pagamentoDTOs);
            }

            return ResponseEntity.ok(pagamentoDTOs);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            throw new CustomException("Ocorreu um erro ao listar os pagamentos.", e);
        }
    }
}
