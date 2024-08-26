package com.example.simulacao_pagamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class PagamentoProcessamentoDTO {

    @NotBlank(message = "O número do cartão é obrigatório.")
    private String numeroCartao;

    @NotBlank(message = "A validade do cartão é obrigatória.")
    private String validadeCartao;

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Long usuarioId;

    private String cupomDesconto;


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

    public String getCupomDesconto() {
        return cupomDesconto;
    }

    public void setCupomDesconto(String cupomDesconto) {
        this.cupomDesconto = cupomDesconto;
    }

}
