package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.ProdutoDTO;
import com.intraviologistica.intravio.model.Produto;
import com.intraviologistica.intravio.repository.ProdutoRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    private static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Transactional
    public List<ProdutoDTO> listaProdutos() {
        List<Produto> produtos = produtoRepository.findAll();
        return produtos.stream()
                .map(this::toDTO)
                .sorted(Comparator.comparing(ProdutoDTO::getCodigo))
                .collect(Collectors.toList());
    }

    @Transactional
    public Produto buscaProdutoPorId(String id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }

    @Transactional
    public ProdutoDTO buscaProdutoPorIdDTO(String id) {
        return toDTO(buscaProdutoPorId(id));
    }

    @Transactional
    public ProdutoDTO salvaProduto(ProdutoDTO produtoDTO) {
        Produto produto = toEntity(produtoDTO);
        produto.setId(getUuid());
        produto.setDataCriacao(LocalDateTime.now());
        Produto produtoSalvo = produtoRepository.save(produto);
        return toDTO(produtoSalvo);
    }

    @Transactional
    public ProdutoDTO atualizaProduto(String id, ProdutoDTO produtoDTO) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        Produto produtoConvertido = toEntity(produtoDTO);
        produtoConvertido.setId(produto.getId());
        produtoConvertido.setDataCriacao(produto.getDataCriacao());
        Produto produtoAtualizado = produtoRepository.save(produtoConvertido);
        return toDTO(produtoAtualizado);
    }

    @Transactional
    public void deletaProduto(String id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        produtoRepository.delete(produto);
    }

    public Produto toEntity(ProdutoDTO dto) {
        Produto produto = new Produto();
        produto.setId(dto.getId());
        produto.setCodigo(dto.getCodigo());
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
        dto.setCodigo(produto.getCodigo());
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