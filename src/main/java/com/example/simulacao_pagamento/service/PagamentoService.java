package com.example.simulacao_pagamento.service;

import com.example.simulacao_pagamento.exception.CustomException;
import com.example.simulacao_pagamento.model.Pagamento;
import com.example.simulacao_pagamento.model.Usuario;
import org.springframework.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.simulacao_pagamento.repository.PagamentoRepository;

import java.util.List;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    @Autowired
    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public Pagamento processarPagamento(Pagamento pagamento) {
        try {
            // Verificações básicas antes de salvar o pagamento
            if (pagamento.getNumeroCartao() == null || pagamento.getNumeroCartao().isEmpty()) {
                throw new CustomException("O número do cartão é obrigatório.");
            }

            if (pagamento.getValidadeCartao() == null || pagamento.getValidadeCartao().isEmpty()) {
                throw new CustomException("A validade do cartão é obrigatória.");
            }

            if (pagamento.getValorPago() <= 0) {
                throw new CustomException("O valor pago deve ser maior que zero.");
            }

            // Tentativa de salvar o pagamento no banco de dados
            return pagamentoRepository.save(pagamento);
        } catch (DataAccessException e) {
            // Captura exceções relacionadas ao acesso ao banco de dados
            throw new CustomException("Erro ao acessar o banco de dados ao processar o pagamento.", e);
        } catch (Exception e) {
            // Captura outras exceções não esperadas
            throw new CustomException("Ocorreu um erro inesperado ao processar o pagamento.", e);
        }
    }
    // Método para listar todos os pagamentos de um usuário
    public List<Pagamento> listarPagamentosPorUsuario(Usuario usuario) {
        return pagamentoRepository.findByUsuario(usuario);
    }
}
