package com.example.simulacao_pagamento.dto;

import com.example.simulacao_pagamento.model.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "O nome do usuário é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome do usuário deve ter entre 3 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "O endereço é obrigatório.")
    @Size(min = 5, max = 200, message = "O endereço deve ter entre 5 e 200 caracteres.")
    private String endereco;

    @NotBlank(message = "As informações de contato são obrigatórias.")
    @Size(min = 10, max = 15, message = "O contato deve ter entre 10 e 15 caracteres.")
    private String contato;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    // Método para converter uma entidade Usuario para UsuarioDTO
    public static UsuarioDTO fromEntity(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEndereco(usuario.getEndereco());
        dto.setContato(usuario.getContato());
        return dto;
    }

    // Método para converter um UsuarioDTO para uma entidade Usuario
    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setId(this.getId());
        usuario.setNome(this.getNome());
        usuario.setEndereco(this.getEndereco());
        usuario.setContato(this.getContato());
        return usuario;
    }
}
