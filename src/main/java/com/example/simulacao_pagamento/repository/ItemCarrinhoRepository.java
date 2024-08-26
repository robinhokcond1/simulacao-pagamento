package com.example.simulacao_pagamento.repository;

import com.example.simulacao_pagamento.model.ItemCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, Long> {

    // Método para buscar todos os itens do carrinho por ID do usuário
    List<ItemCarrinho> findByUsuarioId(Long usuarioId);
}
