package com.example.simulacao_pagamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {

    @NotBlank(message = "O nome do usuário é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome do usuário deve ter entre 3 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "O endereço é obrigatório.")
    @Size(min = 5, max = 200, message = "O endereço deve ter entre 5 e 200 caracteres.")
    private String endereco;

    @NotBlank(message = "As informações de contato são obrigatórias.")
    @Size(min = 10, max = 15, message = "O contato deve ter entre 10 e 15 caracteres.")
    private String contato;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }
}
