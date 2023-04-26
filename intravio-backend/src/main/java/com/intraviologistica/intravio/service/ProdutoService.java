package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.ProdutoDTO;
import com.intraviologistica.intravio.model.Produto;
import com.intraviologistica.intravio.repository.ProdutoRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public List<ProdutoDTO> listarProdutos() {
        List<Produto> produtos = produtoRepository.findAll();
        return produtos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Produto buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }

    @Transactional
    public ProdutoDTO buscarProdutoPorIdDTO(Long id) {
        return toDTO(buscarProdutoPorId(id));
    }

    @Transactional
    public ProdutoDTO salvarProduto(ProdutoDTO produtoDTO) {
        Produto produto = toEntity(produtoDTO);
        produto.setDataCriacao(LocalDateTime.now());
        Produto produtoSalvo = produtoRepository.save(produto);
        return toDTO(produtoSalvo);
    }

    @Transactional
    public ProdutoDTO atualizarProduto(Long id, ProdutoDTO produtoDTO) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        Produto produtoConvertido = toEntity(produtoDTO);
        produtoConvertido.setId(produto.getId());
        produtoConvertido.setDataCriacao(produto.getDataCriacao());
        Produto produtoAtualizado = produtoRepository.save(produtoConvertido);
        return toDTO(produtoAtualizado);
    }

    @Transactional
    public void deletarProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        produtoRepository.delete(produto);
    }

    public Produto toEntity(ProdutoDTO dto) {
        Produto produto = new Produto();
        produto.setId(dto.getId());
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco() == null ? 00.0 : dto.getPreco());
        produto.setPeso(dto.getPeso() == null ? 00.0 : dto.getPeso());
        produto.setFabricante(dto.getFabricante());
        produto.setModelo(dto.getModelo());
        produto.setDataAtualizacao(LocalDateTime.now());
        return produto;
    }

    public ProdutoDTO toDTO(Produto produto) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setPreco(produto.getPreco());
        dto.setPeso(produto.getPeso());
        dto.setFabricante(produto.getFabricante());
        dto.setModelo(produto.getModelo());
        dto.setDataAtualizacao(produto.getDataAtualizacao());
        dto.setDataCriacao(produto.getDataCriacao());
        return dto;
    }
}