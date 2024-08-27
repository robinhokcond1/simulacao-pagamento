package com.example.simulacao_pagamento.repository;

import com.example.simulacao_pagamento.model.Pagamento;
import com.example.simulacao_pagamento.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByUsuario(Usuario usuario);

    Page<Pagamento> findByUsuario(Usuario usuario, Pageable pageable);
}

