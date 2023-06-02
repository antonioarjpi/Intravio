package com.intraviologistica.intravio.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intraviologistica.intravio.dto.TransportadorDTO;
import com.intraviologistica.intravio.model.Transportador;
import com.intraviologistica.intravio.service.TransportadorService;
import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class TransportadorControllerTest {

    @InjectMocks
    private TransportadorController transportadorController;

    @Mock
    private TransportadorService transportadorService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(transportadorController).build();
    }

    @Test
    public void testBuscaTransportadorPorId() throws Exception {
        String transportadorId = "1";
        TransportadorDTO transportadorDTO = getTransportadorDTO1();

        // Mock do serviço
        Mockito.when(transportadorService.buscaTransportadorPorId(transportadorId))
                .thenReturn(transportadorDTO);

        // Executa a requisição GET e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transportadores/{id}", transportadorId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(transportadorDTO.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is(transportadorDTO.getNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.motorista", Matchers.is(transportadorDTO.getMotorista())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.placa", Matchers.is(transportadorDTO.getPlaca())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.veiculo", Matchers.is(transportadorDTO.getVeiculo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.observacao", Matchers.is(transportadorDTO.getObservacao())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cnpj", Matchers.is(transportadorDTO.getCnpj())));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(transportadorService, Mockito.times(1)).buscaTransportadorPorId(transportadorId);
        Mockito.verifyNoMoreInteractions(transportadorService);
    }

    @Test
    public void testListaTransportadores() throws Exception {
        TransportadorDTO transportador1 = getTransportadorDTO1();
        TransportadorDTO transportador2 = getTransportadorDTO2();

        List<TransportadorDTO> transportadorDTOList = Arrays.asList(transportador1, transportador2);

        // Mock do serviço
        Mockito.when(transportadorService.listaTodosTransportadores())
                .thenReturn(transportadorDTOList);

        // Executa a requisição GET e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transportadores"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(transportador1.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome", Matchers.is(transportador1.getNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].motorista", Matchers.is(transportador1.getMotorista())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].placa", Matchers.is(transportador1.getPlaca())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].veiculo", Matchers.is(transportador1.getVeiculo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].observacao", Matchers.is(transportador1.getObservacao())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cnpj", Matchers.is(transportador1.getCnpj())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(transportador2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nome", Matchers.is(transportador2.getNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].motorista", Matchers.is(transportador2.getMotorista())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].placa", Matchers.is(transportador2.getPlaca())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].veiculo", Matchers.is(transportador2.getVeiculo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].observacao", Matchers.is(transportador2.getObservacao())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].cnpj", Matchers.is(transportador2.getCnpj())));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(transportadorService, Mockito.times(1)).listaTodosTransportadores();
        Mockito.verifyNoMoreInteractions(transportadorService);
    }

    @Test
    public void testSalvaTransportador() throws Exception {
        TransportadorDTO transportadorDTO = getTransportadorDTO1();

        Transportador transportadorSalvo = getTransportador();

        // Mock do serviço
        Mockito.when(transportadorService.salvaTransportador(Mockito.any(TransportadorDTO.class)))
                .thenReturn(transportadorSalvo);

        // Executa a requisição POST e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transportadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoToJson(transportadorDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(transportadorSalvo.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is(transportadorSalvo.getNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.motorista", Matchers.is(transportadorSalvo.getMotorista())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.placa", Matchers.is(transportadorSalvo.getPlaca())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.veiculo", Matchers.is(transportadorSalvo.getVeiculo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.observacao", Matchers.is(transportadorSalvo.getObservacao())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cnpj", Matchers.is(transportadorSalvo.getCnpj())));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(transportadorService, Mockito.times(1)).salvaTransportador(Mockito.any(TransportadorDTO.class));
        Mockito.verifyNoMoreInteractions(transportadorService);
    }

    @Test
    public void testAtualizaTransportador() throws Exception {
        String id = "1";
        TransportadorDTO transportadorDTO = getTransportadorDTO1();
        transportadorDTO.setId(id);

        Transportador transportadorAtualizado = getTransportador();

        // Mock do serviço
        Mockito.when(transportadorService.atualizaTransportador(Mockito.any(TransportadorDTO.class)))
                .thenReturn(transportadorAtualizado);

        // Executa a requisição PUT e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/transportadores/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoToJson(transportadorDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(transportadorAtualizado.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is(transportadorAtualizado.getNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.motorista", Matchers.is(transportadorAtualizado.getMotorista())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.placa", Matchers.is(transportadorAtualizado.getPlaca())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.veiculo", Matchers.is(transportadorAtualizado.getVeiculo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.observacao", Matchers.is(transportadorAtualizado.getObservacao())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cnpj", Matchers.is(transportadorAtualizado.getCnpj())));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(transportadorService, Mockito.times(1)).atualizaTransportador(Mockito.any(TransportadorDTO.class));
        Mockito.verifyNoMoreInteractions(transportadorService);
    }

    @Test
    public void testDeletaTransportador() throws Exception {
        // Dado
        String id = "1";

        // Executa a requisição DELETE e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/transportadores/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(transportadorService, Mockito.times(1)).deletaTransportador(id);
        Mockito.verifyNoMoreInteractions(transportadorService);
    }

    private String converteObjetoToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    private static Transportador getTransportador() {
        Transportador transportador = new Transportador();
        transportador.setId("1");
        transportador.setNome("Transportadora ABC");
        transportador.setMotorista("João Silva");
        transportador.setPlaca("ABC-1234");
        transportador.setVeiculo("Caminhão");
        transportador.setObservacao("Entrega urgente");
        transportador.setCnpj("12345678901234");
        return transportador;
    }

    private static TransportadorDTO getTransportadorDTO1() {
        TransportadorDTO transportadorDTO = new TransportadorDTO();
        transportadorDTO.setId("1");
        transportadorDTO.setNome("Transportadora ABC");
        transportadorDTO.setMotorista("João Silva");
        transportadorDTO.setPlaca("ABC-1234");
        transportadorDTO.setVeiculo("Caminhão");
        transportadorDTO.setObservacao("Entrega urgente");
        transportadorDTO.setCnpj("12345678901234");
        return transportadorDTO;
    }

    private static TransportadorDTO getTransportadorDTO2() {
        TransportadorDTO transportadorDTO = new TransportadorDTO();
        transportadorDTO.setId("2");
        transportadorDTO.setNome("Transportadora XYZ");
        transportadorDTO.setMotorista("Maria Oliveira");
        transportadorDTO.setPlaca("XYZ-5678");
        transportadorDTO.setVeiculo("Van");
        transportadorDTO.setObservacao("Entrega programada");
        transportadorDTO.setCnpj("98765432109876");
        return transportadorDTO;
    }
}