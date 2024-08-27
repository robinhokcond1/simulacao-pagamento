package com.example.simulacao_pagamento.controller;

import com.example.simulacao_pagamento.dto.CupomDescontoApplyDTO;
import com.example.simulacao_pagamento.dto.CupomDescontoCreateDTO;
import com.example.simulacao_pagamento.exception.CustomException;
import com.example.simulacao_pagamento.model.CupomDesconto;
import com.example.simulacao_pagamento.service.CupomDescontoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cupom")
@Tag(name = "Cupom de Desconto", description = "API relacionada à aplicação e criação de cupons de desconto")
public class CupomDescontoController {

    private final CupomDescontoService cupomDescontoService;

    @Autowired
    public CupomDescontoController(CupomDescontoService cupomDescontoService) {
        this.cupomDescontoService = cupomDescontoService;
    }

    @Operation(summary = "Listar todos os cupons de desconto", description = "Recupera todos os cupons de desconto existentes na base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cupons listados com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno ao listar os cupons de desconto",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/listar")
    public ResponseEntity<Page<CupomDesconto>> listarTodosCupons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            Page<CupomDesconto> cupons = cupomDescontoService.listarTodosCupons(pageable);
            return ResponseEntity.ok(cupons);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Aplicar um cupom de desconto", description = "Aplica um cupom de desconto a uma compra.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cupom aplicado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Código de cupom inválido ou não encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno ao aplicar o cupom de desconto",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/aplicar")
    public ResponseEntity<String> aplicarCupom(@Validated @RequestBody CupomDescontoApplyDTO cupomDescontoDTO) {
        try {
            String resultado = cupomDescontoService.aplicarCupom(cupomDescontoDTO.getCodigo());
            return ResponseEntity.ok(resultado);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao aplicar o cupom de desconto: " + e.getMessage());
        }
    }

    @Operation(summary = "Criar um novo cupom de desconto", description = "Cria um novo cupom de desconto e o salva na base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cupom criado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno ao criar o cupom de desconto",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/criar")
    public ResponseEntity<String> criarCupom(@Validated @RequestBody CupomDescontoCreateDTO cupomDescontoDTO) {
        try {
            cupomDescontoService.criarCupom(cupomDescontoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cupom criado com sucesso!");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao criar o cupom de desconto: " + e.getMessage());
        }
    }
}
