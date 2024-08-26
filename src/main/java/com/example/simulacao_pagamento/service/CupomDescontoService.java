package com.example.simulacao_pagamento.service;

import com.example.simulacao_pagamento.exception.CustomException;
import com.example.simulacao_pagamento.model.CupomDesconto;
import com.example.simulacao_pagamento.repository.CupomDescontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CupomDescontoService {

    private final CupomDescontoRepository cupomDescontoRepository;
    private static final int MAX_USO_CUPOM = 10;

    @Autowired
    public CupomDescontoService(CupomDescontoRepository cupomDescontoRepository) {
        this.cupomDescontoRepository = cupomDescontoRepository;
    }

    // Método para aplicar um cupom de desconto existente
    public Optional<CupomDesconto> aplicarCupom(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new CustomException("O código do cupom não pode ser nulo ou vazio.");
        }

        // Busca o cupom pelo código
        Optional<CupomDesconto> cupomOptional = cupomDescontoRepository.findByCodigo(codigo);
        if (cupomOptional.isEmpty()) {
            throw new CustomException("Cupom de desconto inválido ou não encontrado.");
        }

        CupomDesconto cupomDesconto = cupomOptional.get();

        // Verifica se o cupom está ativo e ainda não atingiu o limite máximo de utilizações
        if (!cupomDesconto.isAtivo()) {
            throw new CustomException("Cupom expirado: Este cupom não é mais válido.");
        }

        if (cupomDesconto.getNumeroUsos() >= MAX_USO_CUPOM) {
            throw new CustomException("Este cupom de desconto já atingiu o limite máximo de utilizações e não pode mais ser usado.");
        }

        try {
            // Incrementa o número de usos do cupom
            cupomDesconto.incrementarNumeroUsos();

            // Salva as alterações do cupom
            cupomDescontoRepository.save(cupomDesconto);

            return Optional.of(cupomDesconto);
        } catch (DataAccessException e) {
            throw new CustomException("Erro ao acessar o banco de dados ao buscar o cupom de desconto.", e);
        }
    }

    // Método para listar todos os cupons de desconto
    public List<CupomDesconto> listarTodosCupons() {
        try {
            return cupomDescontoRepository.findAll();
        } catch (DataAccessException e) {
            throw new CustomException("Erro ao acessar o banco de dados ao listar os cupons de desconto.", e);
        } catch (Exception e) {
            throw new CustomException("Ocorreu um erro inesperado ao listar os cupons de desconto.", e);
        }
    }

    // Método para criar um novo cupom de desconto
    public CupomDesconto criarCupom(CupomDesconto cupomDesconto) {
        try {
            // Validação básica para verificar se o código do cupom não é nulo ou vazio
            if (cupomDesconto.getCodigo() == null || cupomDesconto.getCodigo().trim().isEmpty()) {
                throw new CustomException("O código do cupom não pode ser nulo ou vazio.");
            }

            // Validação adicional para verificar se a porcentagem de desconto é válida
            if (cupomDesconto.getPorcentagemDesconto() <= 0 || cupomDesconto.getPorcentagemDesconto() > 100) {
                throw new CustomException("A porcentagem de desconto deve ser maior que 0 e menor ou igual a 100.");
            }

            // Inicializa o número de usos do cupom como 0 e define o cupom como ativo
            cupomDesconto.setNumeroUsos(0);
            cupomDesconto.setAtivo(true);

            // Salvando o cupom no banco de dados
            return cupomDescontoRepository.save(cupomDesconto);
        } catch (DataAccessException e) {
            // Captura exceções relacionadas ao acesso ao banco de dados
            throw new CustomException("Erro ao acessar o banco de dados ao criar o cupom de desconto.", e);
        } catch (Exception e) {
            // Captura outras exceções não esperadas
            throw new CustomException("Ocorreu um erro inesperado ao criar o cupom de desconto.", e);
        }
    }
}
