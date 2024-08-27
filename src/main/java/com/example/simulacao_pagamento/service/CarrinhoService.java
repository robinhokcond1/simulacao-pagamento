package com.example.simulacao_pagamento.service;

import com.example.simulacao_pagamento.dto.ItemCarrinhoDTO;
import com.example.simulacao_pagamento.exception.CustomException;
import com.example.simulacao_pagamento.exception.ResourceNotFoundException;
import com.example.simulacao_pagamento.model.ItemCarrinho;
import com.example.simulacao_pagamento.model.Usuario;
import com.example.simulacao_pagamento.repository.ItemCarrinhoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarrinhoService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final UsuarioService usuarioService;

    @Autowired
    public CarrinhoService(ItemCarrinhoRepository itemCarrinhoRepository, UsuarioService usuarioService) {
        this.itemCarrinhoRepository = itemCarrinhoRepository;
        this.usuarioService = usuarioService;
    }

    // Método para listar e formatar os itens do carrinho de um usuário específico
    public String listarItensDoUsuarioFormatado(Long usuarioId) {
        Usuario usuario = usuarioService.obterUsuarioPorId(usuarioId)
                .orElseThrow(() -> new CustomException("Usuário não encontrado."));

        List<ItemCarrinho> itens = listarItensDoUsuario(usuario.getId());
        if (itens.isEmpty()) {
            throw new CustomException("O carrinho do usuário está vazio.");
        }

        double valorTotalCarrinho = itens.stream().mapToDouble(ItemCarrinho::getValorTotal).sum();

        String itensJson = itens.stream()
                .map(item -> String.format("{\n  \"id\": %d,\n  \"nomeProduto\": \"%s\",\n  \"quantidade\": %d,\n  \"preco\": %.2f,\n  \"valorTotal\": %.2f\n}",
                        item.getId(), item.getNomeProduto(), item.getQuantidade(), item.getPreco(), item.getValorTotal()))
                .collect(Collectors.joining(",\n"));

        return "Itens no carrinho: " + itens.size() + " item(s) encontrado(s).\n[\n" + itensJson + "\n]\n" +
                "Valor total do carrinho: R$ " + String.format("%.2f", valorTotalCarrinho);
    }

    // Método para listar os itens do carrinho de um usuário específico
    public List<ItemCarrinho> listarItensDoUsuario(Long usuarioId) {
        return itemCarrinhoRepository.findByUsuarioId(usuarioId);
    }

    // Método para adicionar um item ao carrinho
    public ItemCarrinho adicionarItem(ItemCarrinhoDTO itemDto) {
        Usuario usuario = usuarioService.obterUsuarioPorId(itemDto.getUsuarioId())
                .orElseThrow(() -> new CustomException("Usuário não encontrado."));

        ItemCarrinho item = new ItemCarrinho();
        item.setNomeProduto(itemDto.getNomeProduto());
        item.setQuantidade(itemDto.getQuantidade());
        item.setPreco(itemDto.getPreco());
        item.setUsuario(usuario);

        // Calcular o valor total do item
        item.atualizarValorTotal();

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
    // Método para limpar o carrinho de um usuário usando a query nativa
    @Transactional
    public void limparCarrinho(Long usuarioId) {
        List<ItemCarrinho> itens = itemCarrinhoRepository.findByUsuarioId(usuarioId);
        if (itens.isEmpty()) {
            throw new CustomException("O carrinho do usuário está vazio.");
        } else {
            System.out.println("Itens antes da exclusão: ");
            itens.forEach(item -> System.out.println(item.toString()));

            itemCarrinhoRepository.deleteAllByUsuarioId(usuarioId);

            // Verifique se os itens foram realmente excluídos
            List<ItemCarrinho> itensAposExclusao = itemCarrinhoRepository.findByUsuarioId(usuarioId);
            System.out.println("Itens após a exclusão: ");
            itensAposExclusao.forEach(item -> System.out.println(item.toString()));
        }
    }
}
