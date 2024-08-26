package com.example.simulacao_pagamento.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroCartao;
    private String validadeCartao;
    private double valorPago;

    private String cupomDescontoAplicado;
    private double valorDescontado;

    // Novo campo para armazenar o percentual de desconto
    private double percentualDesconto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    @OneToMany(mappedBy = "pagamento", cascade = CascadeType.ALL)
    private List<ItemCarrinho> itensCarrinho;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    public String getCupomDescontoAplicado() {
        return cupomDescontoAplicado;
    }

    public void setCupomDescontoAplicado(String cupomDescontoAplicado) {
        this.cupomDescontoAplicado = cupomDescontoAplicado;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ItemCarrinho> getItensCarrinho() {
        return itensCarrinho;
    }

    public void setItensCarrinho(List<ItemCarrinho> itensCarrinho) {
        this.itensCarrinho = itensCarrinho;
    }
}
