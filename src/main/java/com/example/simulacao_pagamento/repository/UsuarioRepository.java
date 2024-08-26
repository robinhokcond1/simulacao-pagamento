package com.example.simulacao_pagamento.repository;

import com.example.simulacao_pagamento.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}