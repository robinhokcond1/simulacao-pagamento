package com.example.simulacao_pagamento.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CupomDescontoCreateDTO {

    @NotBlank(message = "O código do cupom é obrigatório.")
    @Size(min = 3, max = 20, message = "O código do cupom deve ter entre 3 e 20 caracteres.")
    private String codigo;

    @NotNull(message = "A porcentagem de desconto é obrigatória.")
    @Min(value = 1, message = "A porcentagem de desconto deve ser maior que 0.")
    @Max(value = 100, message = "A porcentagem de desconto deve ser menor ou igual a 100.")
    private Double porcentagemDesconto;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Double getPorcentagemDesconto() {
        return porcentagemDesconto;
    }

    public void setPorcentagemDesconto(Double porcentagemDesconto) {
        this.porcentagemDesconto = porcentagemDesconto;
    }
}
