package com.example.simulacao_pagamento.controller;

import com.example.simulacao_pagamento.dto.UsuarioDTO;
import com.example.simulacao_pagamento.exception.CustomException;
import com.example.simulacao_pagamento.model.Usuario;
import com.example.simulacao_pagamento.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuário", description = "APIs relacionadas às operações de usuário")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Salvar um novo usuário", description = "Cria e salva um novo usuário na base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno ao salvar o usuário",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<String> salvarUsuario(@Validated @RequestBody UsuarioDTO usuarioDto) {
        try {
            // Convertendo o DTO para a entidade Usuario
            Usuario usuario = new Usuario();
            usuario.setNome(usuarioDto.getNome());
            usuario.setEndereco(usuarioDto.getEndereco());
            usuario.setContato(usuarioDto.getContato());

            // Salva o usuário e retorna uma mensagem customizada
            usuarioService.salvarUsuario(usuario);
            return new ResponseEntity<>("Usuário criado com sucesso.", HttpStatus.CREATED);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new CustomException("Ocorreu um erro ao salvar o usuário.", e);
        }
    }

    @Operation(summary = "Obter um usuário", description = "Obtém os detalhes de um usuário pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar o usuário",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<String> obterUsuario(@PathVariable Long id) {
        try {
            Optional<Usuario> usuario = usuarioService.obterUsuarioPorId(id);
            if (usuario.isPresent()) {
                return ResponseEntity.ok("Usuário encontrado: " + usuario.get().getNome());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }
        } catch (Exception e) {
            throw new CustomException("Ocorreu um erro ao buscar o usuário.", e);
        }
    }

    @Operation(summary = "Atualizar um usuário", description = "Atualiza os detalhes de um usuário existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno ao atualizar o usuário",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarUsuario(@PathVariable Long id, @Validated @RequestBody UsuarioDTO usuarioDto) {
        try {
            Optional<Usuario> usuarioExistente = usuarioService.obterUsuarioPorId(id);
            if (usuarioExistente.isPresent()) {
                Usuario usuario = usuarioExistente.get();
                usuario.setNome(usuarioDto.getNome());
                usuario.setEndereco(usuarioDto.getEndereco());
                usuario.setContato(usuarioDto.getContato());

                usuarioService.salvarUsuario(usuario);
                return ResponseEntity.ok("Usuário atualizado com sucesso.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            throw new CustomException("Ocorreu um erro ao atualizar o usuário.", e);
        }
    }

    @Operation(summary = "Deletar um usuário", description = "Remove um usuário existente da base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno ao deletar o usuário",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id) {
        try {
            Optional<Usuario> usuarioExistente = usuarioService.obterUsuarioPorId(id);
            if (usuarioExistente.isPresent()) {
                usuarioService.deletarUsuario(id);
                return ResponseEntity.ok("Usuário deletado com sucesso.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }
        } catch (Exception e) {
            throw new CustomException("Ocorreu um erro ao deletar o usuário.", e);
        }
    }

    @Operation(summary = "Listar todos os usuários", description = "Recupera todos os usuários existentes na base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno ao listar os usuários",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarTodosUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            throw new CustomException("Ocorreu um erro ao listar os usuários.", e);
        }
    }
}
