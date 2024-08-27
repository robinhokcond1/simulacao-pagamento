package com.example.simulacao_pagamento.service;

import com.example.simulacao_pagamento.dto.CupomDescontoCreateDTO;
import com.example.simulacao_pagamento.exception.CustomException;
import com.example.simulacao_pagamento.model.CupomDesconto;
import com.example.simulacao_pagamento.repository.CupomDescontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CupomDescontoService {

    private final CupomDescontoRepository cupomDescontoRepository;
    private static final int MAX_USO_CUPOM = 10;

    @Autowired
    public CupomDescontoService(CupomDescontoRepository cupomDescontoRepository) {
        this.cupomDescontoRepository = cupomDescontoRepository;
    }

    // Método para aplicar um cupom de desconto existente
    public String aplicarCupom(String codigo) {
        validarCodigoCupom(codigo);

        CupomDesconto cupomDesconto = cupomDescontoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new CustomException("Cupom de desconto inválido ou não encontrado."));

        validarCupomAtivo(cupomDesconto);
        incrementarUsoCupom(cupomDesconto);

        return "Cupom aplicado com sucesso! Desconto de " + cupomDesconto.getPorcentagemDesconto() + "%.";
    }

    // Método para listar todos os cupons de desconto
    public Page<CupomDesconto> listarTodosCupons(Pageable pageable) {
        try {
            return cupomDescontoRepository.findAll(pageable);
        } catch (DataAccessException e) {
            throw new CustomException("Erro ao acessar o banco de dados ao listar os cupons de desconto.", e);
        } catch (Exception e) {
            throw new CustomException("Ocorreu um erro inesperado ao listar os cupons de desconto.", e);
        }
    }

    // Método para criar um novo cupom de desconto
    public void criarCupom(CupomDescontoCreateDTO cupomDescontoDTO) {
        validarCupomDTO(cupomDescontoDTO);

        CupomDesconto novoCupom = new CupomDesconto();
        novoCupom.setCodigo(cupomDescontoDTO.getCodigo());
        novoCupom.setPorcentagemDesconto(cupomDescontoDTO.getPorcentagemDesconto());
        novoCupom.setNumeroUsos(0);
        novoCupom.setAtivo(true);

        try {
            cupomDescontoRepository.save(novoCupom);
        } catch (DataAccessException e) {
            throw new CustomException("Erro ao acessar o banco de dados ao criar o cupom de desconto.", e);
        }
    }

    // Método para buscar a porcentagem de desconto de um cupom
    public double buscarPorcentagemDesconto(String codigo) {
        return cupomDescontoRepository.findByCodigo(codigo)
                .map(CupomDesconto::getPorcentagemDesconto)
                .orElseThrow(() -> new CustomException("Cupom de desconto não encontrado."));
    }

    // Validação do código do cupom
    private void validarCodigoCupom(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new CustomException("O código do cupom não pode ser nulo ou vazio.");
        }
    }

    // Validação se o cupom está ativo e não atingiu o limite de uso
    private void validarCupomAtivo(CupomDesconto cupomDesconto) {
        if (!cupomDesconto.isAtivo()) {
            throw new CustomException("Cupom expirado: Este cupom não é mais válido.");
        }

        if (cupomDesconto.getNumeroUsos() >= MAX_USO_CUPOM) {
            throw new CustomException("Este cupom de desconto já atingiu o limite máximo de utilizações e não pode mais ser usado.");
        }
    }

    // Incremento do número de usos do cupom
    private void incrementarUsoCupom(CupomDesconto cupomDesconto) {
        try {
            cupomDesconto.incrementarNumeroUsos();
            cupomDescontoRepository.save(cupomDesconto);
        } catch (DataAccessException e) {
            throw new CustomException("Erro ao acessar o banco de dados ao aplicar o cupom de desconto.", e);
        }
    }

    // Validação do DTO de criação de cupom
    private void validarCupomDTO(CupomDescontoCreateDTO cupomDescontoDTO) {
        if (cupomDescontoDTO.getCodigo() == null || cupomDescontoDTO.getCodigo().trim().isEmpty()) {
            throw new CustomException("O código do cupom não pode ser nulo ou vazio.");
        }

        if (cupomDescontoDTO.getPorcentagemDesconto() <= 0 || cupomDescontoDTO.getPorcentagemDesconto() > 100) {
            throw new CustomException("A porcentagem de desconto deve ser maior que 0 e menor ou que 100.");
        }
    }
}
