package com.intraviologistica.intravio.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intraviologistica.intravio.dto.PedidoDTO;
import com.intraviologistica.intravio.dto.RomaneioDTO;
import com.intraviologistica.intravio.dto.input.RomaneioFechamentoDTO;
import com.intraviologistica.intravio.dto.input.RomaneioInputDTO;
import com.intraviologistica.intravio.service.RomaneioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RomaneioControllerTest {

    @InjectMocks
    private RomaneioController romaneioController;

    @Mock
    private RomaneioService romaneioService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(romaneioController).build();
    }

    @Test
    public void testListaRomaneios() throws Exception {
        String minDate = "2022-01-01";
        String maxDate = "2022-12-31";

        RomaneioDTO romaneioDTO = getRomaneioDTO();

        List<RomaneioDTO> romaneiosDTO = new ArrayList<>();
        romaneiosDTO.add(romaneioDTO);

        // Configuração do mock
        Mockito.when(romaneioService.listar(minDate, maxDate)).thenReturn(romaneiosDTO);

        // Execução do teste
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/romaneios")
                        .param("minDate", minDate)
                        .param("maxDate", maxDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(romaneiosDTO.size())))
                .andExpect(jsonPath("$[0].id").value(romaneioDTO.getId()))
                .andExpect(jsonPath("$[0].numeroRomaneio").value(romaneioDTO.getNumeroRomaneio()))
                .andExpect(jsonPath("$[0].transportadora").value(romaneioDTO.getTransportadora()))
                .andExpect(jsonPath("$[0].placa").value(romaneioDTO.getPlaca()))
                .andExpect(jsonPath("$[0].veiculo").value(romaneioDTO.getVeiculo()))
                .andExpect(jsonPath("$[0].motorista").value(romaneioDTO.getMotorista()))
                .andExpect(jsonPath("$[0].statusRomaneio").value(romaneioDTO.getStatusRomaneio()))
                .andExpect(jsonPath("$[0].taxaFrete").value(romaneioDTO.getTaxaFrete()))
                .andExpect(jsonPath("$[0].observacao").value(romaneioDTO.getObservacao()))
                .andExpect(jsonPath("$[0].quantidadePedidos").value(romaneioDTO.getQuantidadePedidos()))
                .andExpect(jsonPath("$[0].pesoCarga").value(romaneioDTO.getPesoCarga()))
                .andExpect(jsonPath("$[0].valorCarga").value(romaneioDTO.getValorCarga()))
                .andExpect(jsonPath("$[0].pedidos").isArray())
                .andExpect(jsonPath("$[0].pedidos", hasSize(romaneioDTO.getPedidos().size())));
    }

    @Test
    public void testBuscaRomaneioPorId() throws Exception {
        String romaneioId = "1";

        RomaneioInputDTO romaneioInputDTO = getRomaneioInput();

        Mockito.when(romaneioService.buscarPorId(romaneioId)).thenReturn(romaneioInputDTO);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        // Execução do teste
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/romaneios/{id}", romaneioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(romaneioInputDTO.getId()))
                .andExpect(jsonPath("$.pedidos").isArray())
                .andExpect(jsonPath("$.pedidos.length()").value(romaneioInputDTO.getPedidos().size()))
                .andExpect(jsonPath("$.numeroRomaneio").value(romaneioInputDTO.getNumeroRomaneio()))
                .andExpect(jsonPath("$.transportadorCodigo").value(romaneioInputDTO.getTransportadorCodigo()))
                .andExpect(jsonPath("$.taxaFrete").value(romaneioInputDTO.getTaxaFrete()))
                .andExpect(jsonPath("$.observacao").value(romaneioInputDTO.getObservacao()));
    }

    @Test
    public void testAlterarStatusDeTodosPedidosDoRomaneio() throws Exception {
        String romaneioId = "1";
        Integer novoStatus = 2;

        mockMvc.perform(put("/api/v1/romaneios/{id}/status/{status}", romaneioId, novoStatus))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testProcessarRomaneio() throws Exception {
        String romaneioId = "1";

        mockMvc.perform(put("/api/v1/romaneios/{id}/processar", romaneioId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFecharRomaneio() throws Exception {
        String romaneioId = "1";
        RomaneioFechamentoDTO fechamentoDTO = new RomaneioFechamentoDTO();
        fechamentoDTO.setRomaneioId(romaneioId);
        fechamentoDTO.setPedidosConcluido(Arrays.asList(1, 2, 3));
        fechamentoDTO.setPedidosRetorno(Arrays.asList(4, 5));
        mockMvc.perform(put("/api/v1/romaneios/{id}/fechamento", romaneioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoToJson(fechamentoDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFecharRomaneioCatch() throws Exception {
        String id = "1";
        RomaneioFechamentoDTO dto = new RomaneioFechamentoDTO();

        // Simula uma exceção no serviço
        doThrow(new RuntimeException("Erro ao fechar romaneio")).when(romaneioService).fecharRomaneio(any(RomaneioFechamentoDTO.class));

        mockMvc.perform(put("/api/v1/romaneios/{id}/fechamento", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoToJson(dto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testSalvaRomaneio() throws Exception {
        RomaneioInputDTO romaneioInputDTO = getRomaneioInput();

        RomaneioDTO romaneioDTO = getRomaneioDTO();

        Mockito.when(romaneioService.salvar(any(RomaneioInputDTO.class)))
                .thenReturn(getRomaneioDTO());

        mockMvc.perform(post("/api/v1/romaneios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoToJson(romaneioInputDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(romaneioDTO.getId()))
                .andExpect(jsonPath("$.numeroRomaneio").value(romaneioDTO.getNumeroRomaneio()))
                .andExpect(jsonPath("$.transportadora").value(romaneioDTO.getTransportadora()))
                .andExpect(jsonPath("$.placa").value(romaneioDTO.getPlaca()))
                .andExpect(jsonPath("$.veiculo").value(romaneioDTO.getVeiculo()))
                .andExpect(jsonPath("$.motorista").value(romaneioDTO.getMotorista()))
                .andExpect(jsonPath("$.statusRomaneio").value(romaneioDTO.getStatusRomaneio()))
                .andExpect(jsonPath("$.taxaFrete").value(romaneioDTO.getTaxaFrete()))
                .andExpect(jsonPath("$.observacao").value(romaneioDTO.getObservacao()))
                .andExpect(jsonPath("$.quantidadePedidos").value(romaneioDTO.getQuantidadePedidos()))
                .andExpect(jsonPath("$.pesoCarga").value(romaneioDTO.getPesoCarga()))
                .andExpect(jsonPath("$.valorCarga").value(romaneioDTO.getValorCarga()));
    }

    @Test
    public void testAtualizarRomaneio() throws Exception {
        String id = "1";
        RomaneioInputDTO romaneioInputDTO = getRomaneioInput();

        RomaneioDTO romaneioDTO = getRomaneioDTO();

        Mockito.when(romaneioService.atualizarRomaneio(any(RomaneioInputDTO.class)))
                .thenReturn(getRomaneioDTO());

        mockMvc.perform(put("/api/v1/romaneios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoToJson(romaneioInputDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(romaneioDTO.getId()))
                .andExpect(jsonPath("$.numeroRomaneio").value(romaneioDTO.getNumeroRomaneio()))
                .andExpect(jsonPath("$.transportadora").value(romaneioDTO.getTransportadora()))
                .andExpect(jsonPath("$.placa").value(romaneioDTO.getPlaca()))
                .andExpect(jsonPath("$.veiculo").value(romaneioDTO.getVeiculo()))
                .andExpect(jsonPath("$.motorista").value(romaneioDTO.getMotorista()))
                .andExpect(jsonPath("$.statusRomaneio").value(romaneioDTO.getStatusRomaneio()))
                .andExpect(jsonPath("$.taxaFrete").value(romaneioDTO.getTaxaFrete()))
                .andExpect(jsonPath("$.observacao").value(romaneioDTO.getObservacao()))
                .andExpect(jsonPath("$.quantidadePedidos").value(romaneioDTO.getQuantidadePedidos()))
                .andExpect(jsonPath("$.pesoCarga").value(romaneioDTO.getPesoCarga()))
                .andExpect(jsonPath("$.valorCarga").value(romaneioDTO.getValorCarga()));
    }

    @Test
    public void testExcluiRomaneio() throws Exception {
        String romaneioId = "1";

        mockMvc.perform(delete("/api/v1/romaneios/{id}", romaneioId))
                .andExpect(status().isNoContent());
    }

    private static String converteObjetoToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    public static RomaneioInputDTO getRomaneioInput() {
        RomaneioInputDTO romaneioInputDTO = new RomaneioInputDTO();
        romaneioInputDTO.setId("1");
        romaneioInputDTO.setPedidos(Arrays.asList(1, 2, 3));
        romaneioInputDTO.setNumeroRomaneio(123);
        romaneioInputDTO.setTransportadorCodigo("ABC123");
        romaneioInputDTO.setTaxaFrete(10.0);
        romaneioInputDTO.setObservacao("Observação do romaneio");
        romaneioInputDTO.setProcessa(true);
        return romaneioInputDTO;
    }

    public static RomaneioDTO getRomaneioDTO() {
        RomaneioDTO romaneioDTO = new RomaneioDTO();
        romaneioDTO.setId("1");
        romaneioDTO.setNumeroRomaneio(123);
        romaneioDTO.setTransportadora("Transportadora XYZ");
        romaneioDTO.setPlaca("ABC-1234");
        romaneioDTO.setVeiculo("Caminhão");
        romaneioDTO.setMotorista("João");
        romaneioDTO.setStatusRomaneio(1);
        romaneioDTO.setTaxaFrete(10.0);
        romaneioDTO.setObservacao("Observação do romaneio");
        romaneioDTO.setQuantidadePedidos(3);
        romaneioDTO.setPesoCarga(100.0);
        romaneioDTO.setValorCarga(500.0);

        List<PedidoDTO> pedidos = new ArrayList<>();
        PedidoDTO pedido1 = new PedidoDTO();
        pedido1.setId("1");
        pedidos.add(pedido1);

        PedidoDTO pedido2 = new PedidoDTO();
        pedido2.setId("2");
        pedidos.add(pedido2);

        romaneioDTO.setPedidos(pedidos);

        return romaneioDTO;
    }
}