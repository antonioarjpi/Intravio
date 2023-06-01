package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.ProdutoDTO;
import com.intraviologistica.intravio.model.Produto;
import com.intraviologistica.intravio.repository.ProdutoRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    private ProdutoService produtoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        produtoService = new ProdutoService(produtoRepository);

        Produto produto = getProduto();

        List<Produto> listaProdutos = new ArrayList<>(List.of(produto));

        when(produtoRepository.findAll()).thenReturn(listaProdutos); // Configurando o comportamento esperado do repositório mockado
        when(produtoRepository.findById("1abc")).thenReturn(Optional.of(produto)); // Configurando o comportamento esperado do repositório mockado
        when(produtoRepository.save(any(Produto.class))).thenReturn(getProduto()); // Configurando o comportamento esperado do repositório mockado
    }

    @Test
    public void testListarProdutos_QuandoSucesso() {
        List<ProdutoDTO> resultado = produtoService.listaProdutos();

        assertNotNull(resultado); // Verificando se o resultado não é nulo
        assertEquals(1, resultado.size());  // Verificando se a quantidade de produtos está correta

        // Verificando se a lista de produtos está ordenada pelo nome
        assertEquals("Notebook", resultado.get(0).getNome());
        assertEquals("1abc", resultado.get(0).getId());
        assertEquals("Inspiron 15", resultado.get(0).getModelo());
        assertEquals("Notebook Dell Inspiron 15", resultado.get(0).getDescricao());
        assertEquals("Dell", resultado.get(0).getFabricante());
        assertEquals(123, resultado.get(0).getCodigo());
        assertEquals(3500.00, resultado.get(0).getPreco());
        assertEquals(2.0, resultado.get(0).getPeso());
        assertNotNull(resultado.get(0).getDataAtualizacao());
        assertNotNull(resultado.get(0).getDataCriacao());
    }

    @Test
    void test_buscarProdutoPorId_QuandoEncontrar() {
        Produto produto = getProduto();

        Produto resultado = produtoService.buscaProdutoPorId("1abc");

        assertNotNull(resultado);// Verificando se o resultado não é nulo

        // Verificando se o produto retornado tem os mesmos valores do produto de exemplo
        assertEquals(produto.getId(), resultado.getId());
        assertEquals(produto.getNome(), resultado.getNome());
        assertEquals(produto.getCodigo(), resultado.getCodigo());
        assertEquals(produto.getPeso(), resultado.getPeso());
        assertEquals(produto.getPreco(), resultado.getPreco());
        assertEquals(produto.getDescricao(), resultado.getDescricao());
        assertEquals(produto.getFabricante(), resultado.getFabricante());
        assertEquals(produto.getModelo(), resultado.getModelo());
        assertNotNull(resultado.getDataCriacao());
        assertNotNull(resultado.getDataAtualizacao());
    }

    @Test
    void test_buscarProdutoPorId_QuandoNãoEncontrarProduto() {
        String id = "naoExiste";

        when(produtoRepository.findById(id)).thenReturn(Optional.empty()); //Configurando o comportamento receber valor vazio

        catchThrowableOfType(() -> produtoService.buscaProdutoPorId(id), ResourceNotFoundException.class);
    }


    @Test
    void testBuscarProdutoPorIdRetornandoDTO_QuandoEncontrar() {
        Produto produto = getProduto();

        ProdutoDTO resultado = produtoService.buscaProdutoPorIdDTO("1abc");

        assertNotNull(resultado);// Verificando se o resultado não é nulo

        // Verificando se o produto retornado tem os mesmos valores do produto de exemplo
        assertEquals(produto.getId(), resultado.getId());
        assertEquals(produto.getNome(), resultado.getNome());
        assertEquals(produto.getCodigo(), resultado.getCodigo());
        assertEquals(produto.getPeso(), resultado.getPeso());
        assertEquals(produto.getPreco(), resultado.getPreco());
        assertEquals(produto.getDescricao(), resultado.getDescricao());
        assertEquals(produto.getFabricante(), resultado.getFabricante());
        assertEquals(produto.getModelo(), resultado.getModelo());
        assertNotNull(resultado.getDataCriacao());
        assertNotNull(resultado.getDataAtualizacao());
    }

    @Test
    void testBuscarProdutoPorIdRetornandoDTO_QuandoNãoEncontrarProduto() {
        String id = "naoExiste";

        when(produtoRepository.findById(id)).thenReturn(Optional.empty()); //Configurando o comportamento receber valor vazio

        catchThrowableOfType(() -> produtoService.buscaProdutoPorIdDTO(id), ResourceNotFoundException.class);
    }

    @Test
    public void testSalvarProduto_QuandoSucesso() {
        ProdutoDTO produtoDTO = new ProdutoDTO();  // Criando um produto de exemplo

        produtoService.salvaProduto(produtoDTO); // Chamando o método a ser testado

        verify(produtoRepository, times(1)).save(any(Produto.class)); // Verificando se o método save() do repositório foi chamado uma vez
    }

    @Test
    public void testAtualizarProduto_QuandoSucesso() {
        Produto produto = getProduto(); // Criando um produto de exemplo

        ProdutoDTO produtoAtualizadoDTO = new ProdutoDTO(); // Criando um DTO de produto atualizado

        when(produtoRepository.findById("141aba2")).thenReturn(Optional.of(produto)); // Configurando o comportamento esperado do método buscarProdutoPorId()

        produtoService.atualizaProduto("141aba2", produtoAtualizadoDTO); // Chamando o método a ser testado

        verify(produtoRepository, times(1)).save(any(Produto.class)); // Verificando se o método save() do repositório foi chamado uma vez
    }

    @Test
    public void testDeletaProduto_QuandoSucesso() {
        String produtoId = "1abc";

        Produto produto = getProduto();

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));

        produtoService.deletaProduto(produtoId);

        verify(produtoRepository, times(1)).delete(produto);
    }

    @Test
    void testExcluirProduto_QuandoFalhar() {
        String id = "idNaoExiste";

        when(produtoRepository.findById(anyString())).thenReturn(Optional.empty());

        catchThrowableOfType(() -> produtoService.deletaProduto(id), ResourceNotFoundException.class);

        verify(produtoRepository, times(0)).deleteById(id); // Verificando se o método deleteById() foi chamado uma vez
    }

    @Test
    public void testToEntity() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId("1abc");
        dto.setCodigo(123);
        dto.setNome("Notebook");
        dto.setDescricao("Notebook Dell Inspiron 15");
        dto.setPreco(3500.00);
        dto.setPeso(2.0);
        dto.setFabricante("Dell");
        dto.setModelo("Inspiron 15");

        Produto produto = getProduto();
        Produto convertedProduto = produtoService.toEntity(dto);

        assertEquals(produto.getId(), convertedProduto.getId());
        assertEquals(produto.getCodigo(), convertedProduto.getCodigo());
        assertEquals(produto.getNome(), convertedProduto.getNome());
        assertEquals(produto.getDescricao(), convertedProduto.getDescricao());
        assertEquals(produto.getPreco(), convertedProduto.getPreco(), 0.001);
        assertEquals(produto.getPeso(), convertedProduto.getPeso(), 0.001);
        assertEquals(produto.getFabricante(), convertedProduto.getFabricante());
        assertEquals(produto.getModelo(), convertedProduto.getModelo());
        assertNotNull(convertedProduto.getDataAtualizacao());
    }

    @Test
    public void testToDTO() {
        Produto produto = getProduto();
        ProdutoDTO convertedDto = produtoService.toDTO(produto);

        assertEquals(produto.getId(), convertedDto.getId());
        assertEquals(produto.getCodigo(), convertedDto.getCodigo());
        assertEquals(produto.getNome(), convertedDto.getNome());
        assertEquals(produto.getDescricao(), convertedDto.getDescricao());
        assertEquals(produto.getPreco(), convertedDto.getPreco(), 0.001);
        assertEquals(produto.getPeso(), convertedDto.getPeso(), 0.001);
        assertEquals(produto.getFabricante(), convertedDto.getFabricante());
        assertEquals(produto.getModelo(), convertedDto.getModelo());
        assertEquals(produto.getDataAtualizacao(), convertedDto.getDataAtualizacao());
        assertEquals(produto.getDataCriacao(), convertedDto.getDataCriacao());
    }

    public static Produto getProduto() {
        Produto produto = new Produto();
        produto.setId("1abc");
        produto.setCodigo(123);
        produto.setNome("Notebook");
        produto.setDescricao("Notebook Dell Inspiron 15");
        produto.setPreco(3500.00);
        produto.setPeso(2.0);
        produto.setFabricante("Dell");
        produto.setModelo("Inspiron 15");
        produto.setDataCriacao(LocalDateTime.now());
        produto.setDataAtualizacao(LocalDateTime.now());
        return produto;
    }

    public static ProdutoDTO getProdutoInpuTDTO() {
        ProdutoDTO produto = new ProdutoDTO();
        produto.setId("1abc");
        produto.setCodigo(123);
        produto.setNome("Notebook");
        produto.setDescricao("Notebook Dell Inspiron 15");
        produto.setPreco(3500.00);
        produto.setPeso(2.0);
        produto.setFabricante("Dell");
        produto.setModelo("Inspiron 15");
        produto.setDataCriacao(LocalDateTime.now());
        produto.setDataAtualizacao(LocalDateTime.now());
        return produto;
    }
}