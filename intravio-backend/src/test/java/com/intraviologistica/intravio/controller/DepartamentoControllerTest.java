package com.intraviologistica.intravio.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intraviologistica.intravio.dto.DepartamentoDTO;
import com.intraviologistica.intravio.model.Departamento;
import com.intraviologistica.intravio.service.DepartamentoService;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class DepartamentoControllerTest {

    @InjectMocks
    private DepartamentoController departamentoController;

    @Mock
    private DepartamentoService departamentoService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(departamentoController).build();
    }

    @Test
    public void testListarDepartamentos() throws Exception {
        DepartamentoDTO departamento1 = new DepartamentoDTO("id1", "Departamento 1");
        DepartamentoDTO departamento2 = new DepartamentoDTO("id2", "Departamento 2");
        List<DepartamentoDTO> departamentos = Arrays.asList(departamento1, departamento2);

        // Mock do serviço
        Mockito.when(departamentoService.listarDepartamentos()).thenReturn(departamentos);

        // Executa a requisição GET
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/departamentos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is("id1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome", Matchers.is("Departamento 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is("id2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nome", Matchers.is("Departamento 2")));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(departamentoService, Mockito.times(1)).listarDepartamentos();
        Mockito.verifyNoMoreInteractions(departamentoService);
    }

    @Test
    public void testBuscarDepartamentoPorId() throws Exception {
        String departamentoId = "1";
        DepartamentoDTO departamentoDTO = new DepartamentoDTO("id1", "Departamento 1");

        // Mock do serviço
        Mockito.when(departamentoService.buscarDepartamentoPorIdRetornandoDTO(departamentoId))
                .thenReturn(departamentoDTO);

        // Executa a requisição GET com o ID do departamento e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/departamentos/{id}", departamentoId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is("id1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is("Departamento 1")));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(departamentoService, Mockito.times(1))
                .buscarDepartamentoPorIdRetornandoDTO(departamentoId);
        Mockito.verifyNoMoreInteractions(departamentoService);
    }

    @Test
    public void testSalvarDepartamento() throws Exception {
        DepartamentoDTO departamentoDTO = new DepartamentoDTO(null, "Departamento Teste");

        Departamento departamentoSalvo = new Departamento("id1", "Departamento Teste");

        // Mock do serviço
        Mockito.when(departamentoService.salvarDepartamento(Mockito.any(DepartamentoDTO.class)))
                .thenReturn(departamentoSalvo);

        // Executa a requisição POST com o departamento e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/departamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoToJson(departamentoDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is("id1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is("Departamento Teste")));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(departamentoService, Mockito.times(1))
                .salvarDepartamento(Mockito.any(DepartamentoDTO.class));
        Mockito.verifyNoMoreInteractions(departamentoService);
    }

    @Test
    public void testAtualizarDepartamento() throws Exception {
        DepartamentoDTO departamentoDTO = new DepartamentoDTO("id1", "Departamento Atualizado");

        Departamento departamentoAtualizado = new Departamento("id1", "Departamento Atualizado");

        // Mock do serviço
        Mockito.when(departamentoService.atualizarDepartamento(Mockito.eq("id1"), Mockito.any(DepartamentoDTO.class)))
                .thenReturn(departamentoAtualizado);

        // Executa a requisição PUT com o ID do departamento e o departamento atualizado e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/departamentos/{id}", "id1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoToJson(departamentoDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is("Departamento Atualizado")));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(departamentoService, Mockito.times(1))
                .atualizarDepartamento(Mockito.eq("id1"), Mockito.any(DepartamentoDTO.class));
        Mockito.verifyNoMoreInteractions(departamentoService);
    }

    @Test
    public void testExcluirDepartamento() throws Exception {
        String id = "1";

        // Executa a requisição DELETE com o ID do departamento e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/departamentos/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(departamentoService, Mockito.times(1))
                .excluirDepartamento(id);
        Mockito.verifyNoMoreInteractions(departamentoService);
    }

    private static String converteObjetoToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}