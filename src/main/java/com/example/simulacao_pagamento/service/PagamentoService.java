package com.example.simulacao_pagamento.service;

import com.example.simulacao_pagamento.dto.CupomDescontoApplyDTO;
import com.example.simulacao_pagamento.dto.PagamentoDTO;
import com.example.simulacao_pagamento.dto.PagamentoProcessamentoDTO;
import com.example.simulacao_pagamento.exception.CustomException;
import com.example.simulacao_pagamento.model.HistoricoCompra;
import com.example.simulacao_pagamento.model.ItemCarrinho;
import com.example.simulacao_pagamento.model.Pagamento;
import com.example.simulacao_pagamento.model.Usuario;
import com.example.simulacao_pagamento.repository.HistoricoCompraRepository;
import com.example.simulacao_pagamento.repository.PagamentoRepository;
import com.example.simulacao_pagamento.service.CupomDescontoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final CarrinhoService carrinhoService;
    private final UsuarioService usuarioService;
    private final RestTemplate restTemplate;
    private final HistoricoCompraRepository historicoCompraRepository;
    private final CupomDescontoService cupomDescontoService;

    @Autowired
    public PagamentoService(PagamentoRepository pagamentoRepository, CarrinhoService carrinhoService,
                            UsuarioService usuarioService, RestTemplate restTemplate,
                            HistoricoCompraRepository historicoCompraRepository,
                            CupomDescontoService cupomDescontoService) {
        this.pagamentoRepository = pagamentoRepository;
        this.carrinhoService = carrinhoService;
        this.usuarioService = usuarioService;
        this.restTemplate = restTemplate;
        this.historicoCompraRepository = historicoCompraRepository;
        this.cupomDescontoService = cupomDescontoService;
    }

    @Transactional
    public String processarPagamento(PagamentoProcessamentoDTO pagamentoProcessamentoDTO) {
        Usuario usuario = usuarioService.obterUsuarioPorId(pagamentoProcessamentoDTO.getUsuarioId())
                .orElseThrow(() -> new CustomException("Usuário não encontrado."));

        List<ItemCarrinho> itensCarrinho = carrinhoService.listarItensDoUsuario(usuario.getId());
        if (itensCarrinho.isEmpty()) {
            throw new CustomException("O carrinho do usuário está vazio.");
        }

        double valorTotal = itensCarrinho.stream()
                .mapToDouble(ItemCarrinho::getValorTotal)
                .sum();

        double valorDesconto = 0;
        String cupomDescontoAplicado = null;
        double percentualDesconto = 0;

        if (pagamentoProcessamentoDTO.getCupomDesconto() != null && !pagamentoProcessamentoDTO.getCupomDesconto().isEmpty()) {
            CupomDescontoApplyDTO cupomDescontoApplyDTO = new CupomDescontoApplyDTO();
            cupomDescontoApplyDTO.setCodigo(pagamentoProcessamentoDTO.getCupomDesconto());

            try {
                ResponseEntity<String> response = restTemplate.postForEntity(
                        "http://localhost:8080/cupom/aplicar",
                        new HttpEntity<>(cupomDescontoApplyDTO),
                        String.class
                );

                if (response.getStatusCode() == HttpStatus.OK) {
                    percentualDesconto = cupomDescontoService.buscarPorcentagemDesconto(pagamentoProcessamentoDTO.getCupomDesconto());
                    valorDesconto = valorTotal * (percentualDesconto / 100);
                    valorTotal -= valorDesconto;
                    cupomDescontoAplicado = pagamentoProcessamentoDTO.getCupomDesconto();
                } else {
                    throw new CustomException("Falha ao aplicar o cupom: " + response.getBody());
                }
            } catch (HttpClientErrorException e) {
                throw new CustomException("Erro ao aplicar o cupom de desconto: " + e.getResponseBodyAsString(), e);
            }
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setNumeroCartao(pagamentoProcessamentoDTO.getNumeroCartao());
        pagamento.setValidadeCartao(pagamentoProcessamentoDTO.getValidadeCartao());
        pagamento.setValorPago(valorTotal);
        pagamento.setValorDescontado(valorDesconto);
        pagamento.setCupomDescontoAplicado(cupomDescontoAplicado);
        pagamento.setPercentualDesconto(percentualDesconto);
        pagamento.setUsuario(usuario);

        pagamento.setItensCarrinho(itensCarrinho);
        for (ItemCarrinho item : itensCarrinho) {
            item.setPagamento(pagamento);
        }

        try {
            pagamentoRepository.save(pagamento);

            // Salvar o histórico de compras para cada item
            for (ItemCarrinho item : itensCarrinho) {
                HistoricoCompra historicoCompra = new HistoricoCompra();
                historicoCompra.setNomeProduto(item.getNomeProduto());
                historicoCompra.setQuantidade(item.getQuantidade());
                historicoCompra.setPreco(item.getPreco());
                historicoCompra.setValorTotal(item.getValorTotal());
                historicoCompra.setDataCompra(LocalDateTime.now());
                historicoCompra.setUsuario(usuario);
                historicoCompra.setPagamento(pagamento);

                if (percentualDesconto > 0) {
                    historicoCompra.setPercentualDesconto(percentualDesconto);
                    historicoCompra.setValorDescontado(valorDesconto);
                }

                historicoCompraRepository.save(historicoCompra);
            }

            // Limpar o carrinho após salvar o histórico
            carrinhoService.limparCarrinho(usuario.getId());

        } catch (DataAccessException e) {
            throw new CustomException("Erro ao processar o pagamento no banco de dados.", e);
        }

        return "Compra concluída com sucesso! Valor total após desconto: R$ " + String.format("%.2f", valorTotal)
                + ". Cupom utilizado: " + cupomDescontoAplicado + ". Valor descontado: R$ " + String.format("%.2f", valorDesconto);
    }

    public Page<PagamentoDTO> listarPagamentosPorUsuario(Long usuarioId, Pageable pageable) {
        Usuario usuario = usuarioService.obterUsuarioPorId(usuarioId)
                .orElseThrow(() -> new CustomException("Usuário não encontrado."));

        Page<Pagamento> pagamentos = pagamentoRepository.findByUsuario(usuario, pageable);

        return pagamentos.map(pagamento -> {
            List<HistoricoCompra> historicoCompras = historicoCompraRepository.findByPagamento(pagamento);
            return PagamentoDTO.fromEntity(pagamento, historicoCompras);
        });
    }

    private double extrairPercentualDesconto(String resposta) {
        try {
            String[] partes = resposta.split("Desconto de ");
            if (partes.length > 1) {
                String porcentagem = partes[1].replace("%.", "");
                return Double.parseDouble(porcentagem);
            }
        } catch (Exception e) {
            throw new CustomException("Erro ao extrair o percentual de desconto.", e);
        }
        return 0;
    }
}
