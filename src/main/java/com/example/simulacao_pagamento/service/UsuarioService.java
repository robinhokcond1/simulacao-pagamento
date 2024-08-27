package com.example.simulacao_pagamento.service;

import com.example.simulacao_pagamento.exception.CustomException;
import com.example.simulacao_pagamento.model.Usuario;
import com.example.simulacao_pagamento.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario salvarUsuario(Usuario usuario) {
        try {
            // Validações básicas antes de salvar
            if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
                throw new CustomException("O nome do usuário é obrigatório.");
            }

            if (usuario.getContato() == null || usuario.getContato().isEmpty()) {
                throw new CustomException("As informações de contato são obrigatórias.");
            }

            if (usuario.getEndereco() == null || usuario.getEndereco().isEmpty()) {
                throw new CustomException("O endereço é obrigatório.");
            }

            // Tentativa de salvar o usuário no banco de dados
            return usuarioRepository.save(usuario);
        } catch (DataAccessException e) {
            // Captura exceções relacionadas ao acesso ao banco de dados
            throw new CustomException("Erro ao acessar o banco de dados ao salvar o usuário.", e);
        } catch (Exception e) {
            // Captura outras exceções não esperadas
            throw new CustomException("Ocorreu um erro inesperado ao salvar o usuário.", e);
        }
    }

    public Optional<Usuario> obterUsuarioPorId(Long id) {
        try {
            return usuarioRepository.findById(id);
        } catch (DataAccessException e) {
            throw new CustomException("Erro ao acessar o banco de dados ao buscar o usuário.", e);
        } catch (Exception e) {
            throw new CustomException("Ocorreu um erro inesperado ao buscar o usuário.", e);
        }
    }

    public void deletarUsuario(Long id) {
        try {
            usuarioRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new CustomException("Erro ao acessar o banco de dados ao deletar o usuário.", e);
        } catch (Exception e) {
            throw new CustomException("Ocorreu um erro inesperado ao deletar o usuário.", e);
        }
    }

    // Método para listar todos os usuários com paginação
    public Page<Usuario> listarTodosUsuarios(Pageable pageable) {
        try {
            return usuarioRepository.findAll(pageable);
        } catch (DataAccessException e) {
            throw new CustomException("Erro ao acessar o banco de dados ao listar os usuários.", e);
        } catch (Exception e) {
            throw new CustomException("Ocorreu um erro inesperado ao listar os usuários.", e);
        }
    }

}
