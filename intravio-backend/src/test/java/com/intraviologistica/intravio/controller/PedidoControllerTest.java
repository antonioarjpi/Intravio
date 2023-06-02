package com.intraviologistica.intravio.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intraviologistica.intravio.dto.HistoricoPedidoDTO;
import com.intraviologistica.intravio.dto.ItemDTO;
import com.intraviologistica.intravio.dto.MotivoCancelamentoDTO;
import com.intraviologistica.intravio.dto.PedidoDTO;
import com.intraviologistica.intravio.dto.input.PedidoInputDTO;
import com.intraviologistica.intravio.model.Filial;
import com.intraviologistica.intravio.model.Funcionario;
import com.intraviologistica.intravio.model.Pedido;
import com.intraviologistica.intravio.service.PedidoService;
import com.intraviologistica.intravio.service.exceptions.FileNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class PedidoControllerTest {

    @InjectMocks
    private PedidoController pedidoController;

    @Mock
    private PedidoService pedidoService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(pedidoController).build();
    }

    @Test
    public void testListarPedidos() throws Exception {
        String minDate = "2023-01-01";
        String maxDate = "2023-12-31";

        PedidoDTO pedidoDTO = getPedidoDTO();
        pedidoDTO.setDataPedido(null);
        pedidoDTO.setDataAtualizacao(null);

        List<PedidoDTO> pedidosDTO = new ArrayList<>();
        pedidosDTO.add(pedidoDTO);

        // Mock do serviço
        when(pedidoService.listaPedidos(minDate, maxDate))
                .thenReturn(pedidosDTO);

        // Executa a requisição GET e verifica a resposta
        mockMvc.perform(get("/api/v1/pedidos")
                        .param("minDate", minDate)
                        .param("maxDate", maxDate))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(pedidosDTO)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].numeroPedido").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].statusPedido").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].numeroRomaneio").value(123))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].remetenteNome").value("Remetente"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].remetenteEmail").value("remetente@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].destinatarioNome").value("Destinatário"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].destinatarioEmail").value("destinatario@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].origem").value("Origem"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].destino").value("Destino"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].prioridade").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].acompanhaStatus").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].codigoRastreio").value("ABC123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].pesoPedido").value(10.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].valorPedido").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].itens").isArray()) // Verifica apenas que é um array
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].itens", Matchers.hasSize(1))) // Verifica tamanho do array
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].imagens").isArray()) // Verifica apenas que é um array
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].imagens", Matchers.hasSize(1))) // Verifica tamanho do array
                .andReturn();
    }

    @Test
    public void testListarPedidosByRomaneio() throws Exception {
        String romaneioId = "1";

        List<Pedido> pedidos = new ArrayList<>();

        List<PedidoDTO> pedidoDTOS = pedidos.stream().map(PedidoDTO::new).collect(Collectors.toList());

        // Mock do serviço
        when(pedidoService.listaPedidosPorRomaneioId(romaneioId))
                .thenReturn(pedidos);

        // Executa a requisição GET e verifica a resposta
        mockMvc.perform(get("/api/v1/pedidos/romaneio/{id}", romaneioId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(pedidoDTOS)));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(pedidoService, Mockito.times(1)).listaPedidosPorRomaneioId(romaneioId);
        Mockito.verifyNoMoreInteractions(pedidoService);
    }

    @Test
    public void testListarPedidosPorStatus() throws Exception {
        Integer status = 1;

        List<PedidoDTO> pedidosDTO = new ArrayList<>();

        // Mock do serviço
        when(pedidoService.listaPedidosPorStatus(status))
                .thenReturn(pedidosDTO);

        // Executa a requisição GET e verifica a resposta
        MvcResult result = mockMvc.perform(get("/api/v1/pedidos/status")
                        .param("status", status.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(pedidosDTO)))
                .andReturn();

        Mockito.verify(pedidoService, Mockito.times(1)).listaPedidosPorStatus(status);
        Mockito.verifyNoMoreInteractions(pedidoService);
    }

    @Test
    public void testBuscarPedidoPorIdRetornaDTO() throws Exception {
        String id = "1";

        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setRemetente(new Funcionario());
        pedido.setDestinatario(new Funcionario());
        pedido.setOrigem(new Filial());
        pedido.setDestino(new Filial());

        PedidoInputDTO pedidoInputDTO = new PedidoInputDTO(pedido);
        pedidoInputDTO.setId(id);

        // Mock do serviço
        when(pedidoService.findById(id))
                .thenReturn(pedido);

        // Executa a requisição GET e verifica a resposta
        mockMvc.perform(get("/api/v1/pedidos/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(pedidoService, Mockito.times(1)).findById(id);
        Mockito.verifyNoMoreInteractions(pedidoService);
    }

    @Test
    public void testBuscarPedidoPorId() throws Exception {
        String id = "1";

        PedidoDTO pedidoDTO = getPedidoDTO();

        when(pedidoService.buscarPedidosPorId(id)).thenReturn(pedidoDTO);

        mockMvc.perform(get("/api/v1/pedidos/busca/completa/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numeroPedido").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusPedido").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numeroRomaneio").value(123))
                .andExpect(MockMvcResultMatchers.jsonPath("$.remetenteNome").value("Remetente"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.remetenteEmail").value("remetente@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.destinatarioNome").value("Destinatário"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.destinatarioEmail").value("destinatario@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.origem").value("Origem"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.destino").value("Destino"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataPedido").exists()) // Verifica apenas que o campo existe
                .andExpect(MockMvcResultMatchers.jsonPath("$.dataAtualizacao").exists()) // Verifica apenas que o campo existe
                .andExpect(MockMvcResultMatchers.jsonPath("$.prioridade").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.acompanhaStatus").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.codigoRastreio").value("ABC123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pesoPedido").value(10.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.valorPedido").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.itens").isArray()) // Verifica apenas que é um array
                .andExpect(MockMvcResultMatchers.jsonPath("$.itens", Matchers.hasSize(1))) // Verifica tamanho do array
                .andExpect(MockMvcResultMatchers.jsonPath("$.imagens").isArray()) // Verifica apenas que é um array
                .andExpect(MockMvcResultMatchers.jsonPath("$.imagens", Matchers.hasSize(1))) // Verifica tamanho do array
                .andReturn();
    }

    @Test
    public void testAdicionaImagemPedido() throws Exception {
        String id = "1";
        MockMultipartFile file1 = new MockMultipartFile("file", "imagem1.jpg", "image/jpeg", "conteúdo da imagem".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "imagem2.jpg", "image/jpeg", "conteúdo da imagem".getBytes());

        mockMvc.perform(multipart("/api/v1/pedidos/{id}/imagem/adicionar", id)
                        .file(file1)
                        .file(file2))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();
    }

    @Test
    public void testDeletarPedido() throws Exception {
        String id = "1";
        MotivoCancelamentoDTO motivo = new MotivoCancelamentoDTO();
        motivo.setMotivo("Pedido cancelado");

        ObjectMapper objectMapper = new ObjectMapper();
        String motivoJson = objectMapper.writeValueAsString(motivo);

        mockMvc.perform(delete("/api/v1/pedidos/{id}", id)
                        .content(motivoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();
    }

    @Test
    public void testListaHistoricoDoPedido() throws Exception {
        String codigo = "ABCDE12345";

        List<HistoricoPedidoDTO> historicoPedidoDTOList = new ArrayList<>();

        // Mock do serviço
        when(pedidoService.listaHistoricoDoPedido(codigo))
                .thenReturn(historicoPedidoDTOList);

        // Executa a requisição GET e verifica a resposta
        mockMvc.perform(get("/api/v1/pedidos/rastreio/{codigo}", codigo))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(historicoPedidoDTOList)))
                .andReturn();

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(pedidoService, Mockito.times(1)).listaHistoricoDoPedido(codigo);
        Mockito.verifyNoMoreInteractions(pedidoService);
    }

    @Test
    public void testBaixarImagens_QuandoSucesso() throws Exception {
        String id = "ABCDE12345";

        // Mock do HttpServletResponse
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        // Mock do serviço
        Mockito.doNothing().when(pedidoService).baixarImagensPedido(eq(id), any(HttpServletResponse.class));

        // Executa a requisição GET e verifica a resposta
        mockMvc.perform(get("/api/v1/pedidos/{id}/download/imagens", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(pedidoService, Mockito.times(1)).baixarImagensPedido(eq(id), any(HttpServletResponse.class));
        Mockito.verifyNoMoreInteractions(pedidoService);
    }

    @Test
    public void testExibeImagensPedido_FileNotFoundException() throws Exception {
        String filename = "image.jpg";

        doThrow(FileNotFoundException.class).when(pedidoService).getImagens(eq(filename), any(HttpServletResponse.class));

        // Executa a requisição GET e verifica a resposta
        mockMvc.perform(get("/api/v1/pedidos/{filename}/imagens", filename))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(pedidoService, Mockito.times(1)).getImagens(eq(filename), any(HttpServletResponse.class));
        Mockito.verifyNoMoreInteractions(pedidoService);
    }

    @Test
    public void testSalvarPedido() throws Exception {
        PedidoInputDTO pedidoInputDTO = new PedidoInputDTO();

        // Mock do serviço
        Pedido pedidoSalvo = new Pedido();

        when(pedidoService.salvarPedido(any(PedidoInputDTO.class))).thenReturn(pedidoSalvo);

        // Executa a requisição POST e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoToJson(pedidoInputDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.containsString("/api/v1/pedidos/")))
                .andReturn();

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(pedidoService, Mockito.times(1)).salvarPedido(any(PedidoInputDTO.class));
        Mockito.verifyNoMoreInteractions(pedidoService);
    }

    @Test
    public void testAtualizarPedido() throws Exception {
        String id = "1";
        PedidoInputDTO pedidoInputDTO = new PedidoInputDTO();
        pedidoInputDTO.setId("1");

        // Mock do serviço
        Pedido pedidoAtualizado = new Pedido();
        when(pedidoService.atualizaPedido(any(PedidoInputDTO.class))).thenReturn(pedidoAtualizado);

        // Executa a requisição PUT e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/pedidos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoToJson(pedidoInputDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(pedidoService, Mockito.times(1)).atualizaPedido(any(PedidoInputDTO.class));
        Mockito.verifyNoMoreInteractions(pedidoService);
    }

    @Test
    public void testExibeImagensPedido() throws Exception {
        String filename = "image.jpg";

        // Mock do serviço
        doNothing().when(pedidoService).getImagens(eq(filename), any(HttpServletResponse.class));

        mockMvc.perform(get("/api/v1/pedidos/{filename}/imagens", filename))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(pedidoService, Mockito.times(1)).getImagens(eq(filename), any(HttpServletResponse.class));
        Mockito.verifyNoMoreInteractions(pedidoService);
    }

    private String converteObjetoToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    private static PedidoDTO getPedidoDTO() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setId("1");
        pedidoDTO.setNumeroPedido(1);
        pedidoDTO.setStatusPedido(2);
        pedidoDTO.setNumeroRomaneio(123);
        pedidoDTO.setRemetenteNome("Remetente");
        pedidoDTO.setRemetenteEmail("remetente@example.com");
        pedidoDTO.setDestinatarioNome("Destinatário");
        pedidoDTO.setDestinatarioEmail("destinatario@example.com");
        pedidoDTO.setOrigem("Origem");
        pedidoDTO.setDestino("Destino");
        pedidoDTO.setDataPedido(LocalDateTime.now());
        pedidoDTO.setDataAtualizacao(LocalDateTime.now());
        pedidoDTO.setPrioridade(1);
        pedidoDTO.setAcompanhaStatus(0);
        pedidoDTO.setCodigoRastreio("ABC123");
        pedidoDTO.setPesoPedido(10.0);
        pedidoDTO.setValorPedido(100.0);
        pedidoDTO.setItens(Collections.singletonList(new ItemDTO()));
        pedidoDTO.setImagens(Collections.singletonList("imagem.jpg"));
        return pedidoDTO;
    }
}