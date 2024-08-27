package com.example.simulacao_pagamento.repository;

import com.example.simulacao_pagamento.model.ItemCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, Long> {

    // Método para buscar todos os itens do carrinho por ID do usuário
    List<ItemCarrinho> findByUsuarioId(Long usuarioId);

    // Método para deletar todos os itens do carrinho de um usuário específico usando query nativa
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM item_carrinho WHERE usuario_id = :usuarioId", nativeQuery = true)
    void deleteAllByUsuarioId(Long usuarioId);
}
