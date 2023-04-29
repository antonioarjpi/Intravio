package com.intraviologistica.intravio.controller;

import com.intraviologistica.intravio.dto.input.ProdutoInputDTO;
import com.intraviologistica.intravio.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoInputDTO>> listarProdutos() {
        List<ProdutoInputDTO> produtos = produtoService.listaProdutos();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoInputDTO> buscarProdutoPorId(@PathVariable String id) {
        ProdutoInputDTO produto = produtoService.buscaProdutoPorIdDTO(id);
        return ResponseEntity.ok(produto);
    }

    @PostMapping
    public ResponseEntity<ProdutoInputDTO> criarProduto(@RequestBody ProdutoInputDTO produtoInputDTO) {
        ProdutoInputDTO produtoCriado = produtoService.salvaProduto(produtoInputDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(produtoCriado.getId()).toUri();
        return ResponseEntity.created(location).body(produtoCriado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoInputDTO> atualizarProduto(@PathVariable String id, @RequestBody ProdutoInputDTO produtoInputDTO) {
        ProdutoInputDTO produtoAtualizado = produtoService.atualizaProduto(id, produtoInputDTO);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable String id) {
        produtoService.deletaProduto(id);
        return ResponseEntity.noContent().build();
    }
}
