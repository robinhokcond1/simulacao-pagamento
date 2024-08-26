package com.example.simulacao_pagamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CupomDescontoApplyDTO {

    @NotBlank(message = "O código do cupom é obrigatório.")
    @Size(min = 3, max = 20, message = "O código do cupom deve ter entre 3 e 20 caracteres.")
    private String codigo;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
