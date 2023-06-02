package com.intraviologistica.intravio.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intraviologistica.intravio.dto.ProdutoDTO;
import com.intraviologistica.intravio.model.Produto;
import com.intraviologistica.intravio.service.ProdutoService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerTest {

    @InjectMocks
    private ProdutoController produtoController;

    @Mock
    private ProdutoService produtoService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(produtoController).build();
    }

    @Test
    public void testListarProdutos() throws Exception {
        ProdutoDTO produto1 = getProdutoDTO1();

        ProdutoDTO produto2 = getProdutoDTO2();

        List<ProdutoDTO> listaProdutos = Arrays.asList(produto1, produto2);

        // Mock do serviço
        when(produtoService.listaProdutos()).thenReturn(listaProdutos);

        // Executa a requisição GET e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(produto1.getId())))
                .andExpect(jsonPath("$[0].nome", Matchers.is(produto1.getNome())))
                .andExpect(jsonPath("$[0].codigo", Matchers.is(produto1.getCodigo())))
                .andExpect(jsonPath("$[1].id", Matchers.is(produto2.getId())))
                .andExpect(jsonPath("$[1].nome", Matchers.is(produto2.getNome())))
                .andExpect(jsonPath("$[1].codigo", Matchers.is(produto2.getCodigo())));

        // Verifica se o método do serviço foi chamado corretamente
        verify(produtoService, times(1)).listaProdutos();
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    public void testBuscarProdutoPorId() throws Exception {
        String produtoId = "1";

        ProdutoDTO produto = getProdutoDTO1();

        // Mock do serviço
        when(produtoService.buscaProdutoPorIdDTO(produtoId)).thenReturn(produto);

        // Executa a requisição GET com o ID do produto e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/produtos/{id}", produtoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(produto.getId())))
                .andExpect(jsonPath("$.nome", Matchers.is(produto.getNome())))
                .andExpect(jsonPath("$.codigo", Matchers.is(produto.getCodigo())));

        // Verifica se o método do serviço foi chamado corretamente
        verify(produtoService, times(1)).buscaProdutoPorIdDTO(produtoId);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    public void testCriarProduto() throws Exception {
        ProdutoDTO produtoDTO = getProdutoDTO1();

        // Mock do serviço
        when(produtoService.salvaProduto(any(ProdutoDTO.class))).thenReturn(produtoDTO);

        // Executa a requisição POST com o objeto produtoDTO e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoToJson(produtoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(produtoDTO.getId())))
                .andExpect(jsonPath("$.nome", Matchers.is(produtoDTO.getNome())))
                .andExpect(jsonPath("$.codigo", Matchers.is(produtoDTO.getCodigo())));

        // Verifica se o método do serviço foi chamado corretamente
        verify(produtoService, times(1)).salvaProduto(any(ProdutoDTO.class));
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    public void testAtualizarProduto() throws Exception {
        String produtoId = "1";
        ProdutoDTO produtoDTO = getProdutoDTO1();

        ProdutoDTO produtoAtualizado = getProdutoDTO1();

        // Mock do serviço
        when(produtoService.atualizaProduto(eq(produtoId), any(ProdutoDTO.class))).thenReturn(produtoAtualizado);

        // Executa a requisição PUT com o objeto produtoDTO e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/produtos/{id}", produtoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(produtoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(produtoAtualizado.getId())))
                .andExpect(jsonPath("$.nome", Matchers.is(produtoAtualizado.getNome())))
                .andExpect(jsonPath("$.codigo", Matchers.is(produtoAtualizado.getCodigo())));

        // Verifica se o método do serviço foi chamado corretamente
        verify(produtoService, times(1)).atualizaProduto(eq(produtoId), any(ProdutoDTO.class));
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    public void testDeletarProduto() throws Exception {
        String produtoId = "1";

        // Executa a requisição DELETE e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/produtos/{id}", produtoId))
                .andExpect(status().isNoContent());

        // Verifica se o método do serviço foi chamado corretamente
        verify(produtoService, times(1)).deletaProduto(produtoId);
        verifyNoMoreInteractions(produtoService);
    }

    private String converteObjetoToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    private static Produto getProduto() {
        Produto produto = new Produto();
        produto.setId("1");
        produto.setNome("Produto A");
        produto.setCodigo(12345);
        produto.setDescricao("Descrição do produto");
        produto.setPreco(9.99);
        produto.setPeso(0.5);
        produto.setFabricante("Fabricante A");
        produto.setModelo("Modelo X");
        produto.setDataCriacao(LocalDateTime.now());
        produto.setDataAtualizacao(LocalDateTime.now());
        return produto;
    }

    private static ProdutoDTO getProdutoDTO1() {
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId("1");
        produtoDTO.setNome("Produto A");
        produtoDTO.setCodigo(12345);
        produtoDTO.setDescricao("Descrição do produto");
        produtoDTO.setPreco(9.99);
        produtoDTO.setPeso(0.5);
        produtoDTO.setFabricante("Fabricante A");
        produtoDTO.setModelo("Modelo X");
        return produtoDTO;
    }

    private static ProdutoDTO getProdutoDTO2() {
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setId("2");
        produtoDTO.setNome("Produto B");
        produtoDTO.setCodigo(67890);
        produtoDTO.setDescricao("Descrição do outro produto");
        produtoDTO.setPreco(19.99);
        produtoDTO.setPeso(1.0);
        produtoDTO.setFabricante("Fabricante B");
        produtoDTO.setModelo("Modelo Y");
        produtoDTO.setDataCriacao(LocalDateTime.now());
        produtoDTO.setDataAtualizacao(LocalDateTime.now());
        return produtoDTO;
    }
}