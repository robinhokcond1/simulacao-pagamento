package com.example.simulacao_pagamento.dto;

import com.example.simulacao_pagamento.model.ItemCarrinho;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ItemCarrinhoDTO {

    @NotBlank(message = "O nome do produto é obrigatório.")
    private String nomeProduto;

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade deve ser maior que zero.")
    private Integer quantidade;

    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero.")
    private Double preco;

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Long usuarioId;

    private double valorTotal;

    // Getters e Setters

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    // Método para converter uma entidade ItemCarrinho para ItemCarrinhoDTO
    public static ItemCarrinhoDTO fromEntity(ItemCarrinho itemCarrinho) {
        ItemCarrinhoDTO dto = new ItemCarrinhoDTO();
        dto.setNomeProduto(itemCarrinho.getNomeProduto());
        dto.setQuantidade(itemCarrinho.getQuantidade());
        dto.setPreco(itemCarrinho.getPreco());
        dto.setUsuarioId(itemCarrinho.getUsuario().getId());
        dto.setValorTotal(itemCarrinho.getValorTotal());
        return dto;
    }
}
