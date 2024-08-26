package com.example.simulacao_pagamento.service;

import com.example.simulacao_pagamento.exception.ResourceNotFoundException;
import com.example.simulacao_pagamento.model.ItemCarrinho;
import com.example.simulacao_pagamento.repository.ItemCarrinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarrinhoService {

    private final ItemCarrinhoRepository itemCarrinhoRepository;

    @Autowired
    public CarrinhoService(ItemCarrinhoRepository itemCarrinhoRepository) {
        this.itemCarrinhoRepository = itemCarrinhoRepository;
    }

    // Método para listar todos os itens do carrinho
    public List<ItemCarrinho> listarItens() {
        return itemCarrinhoRepository.findAll();
    }

    // Método para listar os itens do carrinho de um usuário específico
    public List<ItemCarrinho> listarItensDoUsuario(Long usuarioId) {
        return itemCarrinhoRepository.findByUsuarioId(usuarioId);
    }

    // Método para adicionar um item ao carrinho
    public ItemCarrinho adicionarItem(ItemCarrinho item) {
        return itemCarrinhoRepository.save(item);
    }

    // Método para remover um item do carrinho pelo ID
    public void removerItem(Long id) {
        Optional<ItemCarrinho> item = itemCarrinhoRepository.findById(id);
        if (item.isPresent()) {
            itemCarrinhoRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Item do carrinho não encontrado com o id: " + id);
        }
    }
}
