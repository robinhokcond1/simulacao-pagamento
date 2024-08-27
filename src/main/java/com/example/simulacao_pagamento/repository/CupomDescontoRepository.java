package com.example.simulacao_pagamento.repository;

import com.example.simulacao_pagamento.model.CupomDesconto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, Long> {

    // Método para buscar um cupom pelo código
    Optional<CupomDesconto> findByCodigo(String codigo);

    // Método para buscar todos os cupons com paginação
    Page<CupomDesconto> findAll(Pageable pageable);
}
