package com.example.simulacao_pagamento.repository;

import com.example.simulacao_pagamento.model.CupomDesconto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, Long> {
    Optional<CupomDesconto> findByCodigo(String codigo);
}
