package com.example.simulacao_pagamento.dto;

import com.example.simulacao_pagamento.model.HistoricoCompra;
import jakarta.validation.constraints.NotNull;

public class HistoricoCompraDTO {

    @NotNull(message = "O ID do item é obrigatório.")
    private Long itemId;

    @NotNull(message = "O nome do produto é obrigatório.")
    private String nomeProduto;

    @NotNull(message = "A quantidade é obrigatória.")
    private int quantidade;

    @NotNull(message = "O preço é obrigatório.")
    private double preco;

    @NotNull(message = "O valor total é obrigatório.")
    private double valorTotal;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    // Método para converter uma entidade HistoricoCompra para HistoricoCompraDTO
    public static HistoricoCompraDTO fromEntity(HistoricoCompra historicoCompra) {
        HistoricoCompraDTO dto = new HistoricoCompraDTO();
        dto.setItemId(historicoCompra.getId());
        dto.setNomeProduto(historicoCompra.getNomeProduto());
        dto.setQuantidade(historicoCompra.getQuantidade());
        dto.setPreco(historicoCompra.getPreco());
        dto.setValorTotal(historicoCompra.getValorTotal());
        return dto;
    }
}

