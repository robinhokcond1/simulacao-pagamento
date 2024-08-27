package com.example.simulacao_pagamento.repository;

import com.example.simulacao_pagamento.model.HistoricoCompra;
import com.example.simulacao_pagamento.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoCompraRepository extends JpaRepository<HistoricoCompra, Long> {
    List<HistoricoCompra> findByPagamento(Pagamento pagamento);
}
