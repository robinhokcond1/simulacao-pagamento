package com.example.simulacao_pagamento.dto;

import com.example.simulacao_pagamento.model.Pagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class PagamentoDTO {

    @NotBlank(message = "O número do cartão é obrigatório.")
    private String numeroCartao;

    @NotBlank(message = "A validade do cartão é obrigatória.")
    private String validadeCartao;

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Long usuarioId;

    private String cupomDesconto;
    private double valorPago;
    private double valorDescontado;
    private double percentualDesconto;
    private List<ItemCarrinhoDTO> itensCarrinho;

    // Getters and Setters

    public String getCupomDesconto() {
        return cupomDesconto;
    }

    public void setCupomDesconto(String cupomDesconto) {
        this.cupomDesconto = cupomDesconto;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getValidadeCartao() {
        return validadeCartao;
    }

    public void setValidadeCartao(String validadeCartao) {
        this.validadeCartao = validadeCartao;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    public double getValorDescontado() {
        return valorDescontado;
    }

    public void setValorDescontado(double valorDescontado) {
        this.valorDescontado = valorDescontado;
    }

    public double getPercentualDesconto() {
        return percentualDesconto;
    }

    public void setPercentualDesconto(double percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    public List<ItemCarrinhoDTO> getItensCarrinho() {
        return itensCarrinho;
    }

    public void setItensCarrinho(List<ItemCarrinhoDTO> itensCarrinho) {
        this.itensCarrinho = itensCarrinho;
    }

    // Método para converter uma entidade Pagamento para PagamentoDTO
    public static PagamentoDTO fromEntity(Pagamento pagamento) {
        PagamentoDTO dto = new PagamentoDTO();
        dto.setNumeroCartao(pagamento.getNumeroCartao());
        dto.setValidadeCartao(pagamento.getValidadeCartao());
        dto.setUsuarioId(pagamento.getUsuario().getId());
        dto.setValorPago(pagamento.getValorPago());
        dto.setValorDescontado(pagamento.getValorDescontado());
        dto.setPercentualDesconto(pagamento.getPercentualDesconto());
        dto.setCupomDesconto(pagamento.getCupomDescontoAplicado());
        return dto;
    }
}
