package com.example.simulacao_pagamento.controller;

import com.example.simulacao_pagamento.dto.CupomDescontoApplyDTO;
import com.example.simulacao_pagamento.dto.CupomDescontoCreateDTO;
import com.example.simulacao_pagamento.exception.CustomException;
import com.example.simulacao_pagamento.model.CupomDesconto;
import com.example.simulacao_pagamento.service.CupomDescontoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cupom")
@Tag(name = "Cupom de Desconto", description = "API relacionada à aplicação e criação de cupons de desconto")
public class CupomDescontoController {

    private final CupomDescontoService cupomDescontoService;

    @Autowired
    public CupomDescontoController(CupomDescontoService cupomDescontoService) {
        this.cupomDescontoService = cupomDescontoService;
    }

    @Operation(summary = "Listar todos os cupons de desconto", description = "Recupera todos os cupons de desconto existentes na base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cupons listados com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno ao listar os cupons de desconto",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/listar")
    public ResponseEntity<List<CupomDesconto>> listarTodosCupons() {
        try {
            List<CupomDesconto> cupons = cupomDescontoService.listarTodosCupons();
            return ResponseEntity.ok(cupons);
        } catch (Exception e) {
            throw new CustomException("Ocorreu um erro ao listar os cupons de desconto.", e);
        }
    }

    @Operation(summary = "Aplicar um cupom de desconto", description = "Aplica um cupom de desconto a uma compra.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cupom aplicado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Código de cupom inválido ou não encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno ao aplicar o cupom de desconto",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/aplicar")
    public ResponseEntity<String> aplicarCupom(@Validated @RequestBody CupomDescontoApplyDTO cupomDescontoDTO) {
        try {
            // Chama o serviço para aplicar o cupom
            Optional<CupomDesconto> cupomOptional = cupomDescontoService.aplicarCupom(cupomDescontoDTO.getCodigo());

            // Como a validação já ocorre no serviço, só precisamos lidar com a resposta do serviço
            if (cupomOptional.isPresent()) {
                CupomDesconto cupom = cupomOptional.get();
                String resultado = "Cupom aplicado com sucesso! Desconto de " + cupom.getPorcentagemDesconto() + "%.";
                return ResponseEntity.ok(resultado);
            } else {
                // Isso é apenas uma precaução; o fluxo normal deve lançar uma exceção antes de chegar aqui.
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cupom de desconto inválido ou não encontrado.");
            }
        } catch (CustomException e) {
            // Se o serviço lançar um CustomException, retornamos a mensagem de erro apropriada.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Captura de qualquer outra exceção inesperada
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao aplicar o cupom de desconto: " + e.getMessage());
        }
    }


    @Operation(summary = "Criar um novo cupom de desconto", description = "Cria um novo cupom de desconto e o salva na base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cupom criado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno ao criar o cupom de desconto",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/criar")
    public ResponseEntity<String> criarCupom(@Validated @RequestBody CupomDescontoCreateDTO cupomDescontoDTO) {
        try {
            CupomDesconto novoCupom = new CupomDesconto();
            novoCupom.setCodigo(cupomDescontoDTO.getCodigo());
            novoCupom.setPorcentagemDesconto(cupomDescontoDTO.getPorcentagemDesconto());

            cupomDescontoService.criarCupom(novoCupom);

            return ResponseEntity.status(HttpStatus.CREATED).body("Cupom criado com sucesso!");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            throw new CustomException("Ocorreu um erro ao criar o cupom de desconto.", e);
        }
    }
}
