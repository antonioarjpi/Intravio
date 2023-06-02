package com.intraviologistica.intravio.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intraviologistica.intravio.dto.FuncionarioDTO;
import com.intraviologistica.intravio.dto.input.FuncionarioInputDTO;
import com.intraviologistica.intravio.service.FuncionarioService;
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
class FuncionarioControllerTest {

    @InjectMocks
    private FuncionarioController funcionarioController;

    @Mock
    private FuncionarioService funcionarioService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(funcionarioController).build();
    }

    @Test
    public void testListarFuncionarios() throws Exception {
        FuncionarioDTO funcionario1 = getFuncionarioDTO1();

        FuncionarioDTO funcionario2 = getFuncionarioDTO2();

        List<FuncionarioDTO> listaFuncionarios = Arrays.asList(funcionario1, funcionario2);

        // Mock do serviço
        Mockito.when(funcionarioService.listaFuncionarios())
                .thenReturn(listaFuncionarios);

        // Executa a requisição GET e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/funcionarios"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(funcionario1.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome", Matchers.is(funcionario1.getNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is(funcionario1.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].departamento", Matchers.is(funcionario1.getDepartamento())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].filial", Matchers.is(funcionario1.getFilial())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(funcionario2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nome", Matchers.is(funcionario2.getNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email", Matchers.is(funcionario2.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].filial", Matchers.is(funcionario2.getFilial())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].departamento", Matchers.is(funcionario2.getDepartamento())));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(funcionarioService, Mockito.times(1)).listaFuncionarios();
        Mockito.verifyNoMoreInteractions(funcionarioService);
    }

    @Test
    public void testBuscarFuncionarioPorId() throws Exception {
        String funcionarioId = "1";
        FuncionarioDTO funcionario = getFuncionarioDTO1();
        Mockito.when(funcionarioService.buscaFuncionarioPorId(funcionarioId)).thenReturn(funcionario);

        // Executa a requisição GET com o ID do funcionário
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/funcionarios/{id}", funcionarioId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(funcionario.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is(funcionario.getNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(funcionario.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.filial", Matchers.is(funcionario.getFilial())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.departamento", Matchers.is(funcionario.getDepartamento())));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(funcionarioService, Mockito.times(1)).buscaFuncionarioPorId(funcionarioId);
        Mockito.verifyNoMoreInteractions(funcionarioService);
    }

    @Test
    public void testSalvaFuncionario() throws Exception {
        FuncionarioInputDTO funcionarioInputDTO = getFuncionarioInputDTO();

        FuncionarioDTO savedFuncionario = getFuncionarioDTO1();

        Mockito.when(funcionarioService.salvaFuncionario(Mockito.any(FuncionarioInputDTO.class))).thenReturn(savedFuncionario);

        // Executa a requisição POST com o funcionário de entrada
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoToJson(funcionarioInputDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(savedFuncionario.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is(savedFuncionario.getNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(savedFuncionario.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.departamento", Matchers.is(savedFuncionario.getDepartamento())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.filial", Matchers.is(savedFuncionario.getFilial())));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(funcionarioService, Mockito.times(1))
                .salvaFuncionario(Mockito.any(FuncionarioInputDTO.class));
        Mockito.verifyNoMoreInteractions(funcionarioService);
    }

    @Test
    public void testAtualizaFuncionario() throws Exception {
        // Dado de exemplo
        String funcionarioId = "1";
        FuncionarioInputDTO funcionarioInputDTO = getFuncionarioInputDTO();
        FuncionarioDTO updatedFuncionario = getFuncionarioDTO1();
        Mockito.when(funcionarioService.atualizaFuncionario(Mockito.eq("1"), Mockito.any(FuncionarioInputDTO.class))).thenReturn(updatedFuncionario);

        // Executa a requisição PUT com o ID do funcionário e o funcionário de entrada
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/funcionarios/{id}", funcionarioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoToJson(funcionarioInputDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(updatedFuncionario.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is(updatedFuncionario.getNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(updatedFuncionario.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.departamento", Matchers.is(updatedFuncionario.getDepartamento())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.filial", Matchers.is(updatedFuncionario.getFilial())));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(funcionarioService, Mockito.times(1)).atualizaFuncionario(Mockito.eq("1"), Mockito.any(FuncionarioInputDTO.class));
        Mockito.verifyNoMoreInteractions(funcionarioService);
    }

    @Test
    public void testDeletaFuncionario() throws Exception {
        String funcionarioId = "1";

        // Executa a requisição DELETE com o ID do funcionário
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/funcionarios/{id}", funcionarioId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(funcionarioService, Mockito.times(1)).deletarFuncionario(funcionarioId);
        Mockito.verifyNoMoreInteractions(funcionarioService);
    }

    private String converteObjetoToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    private static FuncionarioInputDTO getFuncionarioInputDTO() {
        FuncionarioInputDTO funcionarioDTO = new FuncionarioInputDTO();
        funcionarioDTO.setId("1");
        funcionarioDTO.setEmail("antonio@email.com");
        funcionarioDTO.setNome("Antonio");
        funcionarioDTO.setFilial(1L);
        funcionarioDTO.setDepartamento("1");
        return funcionarioDTO;
    }

    private static FuncionarioDTO getFuncionarioDTO1() {
        FuncionarioDTO funcionarioDTO = new FuncionarioDTO();
        funcionarioDTO.setId("1");
        funcionarioDTO.setEmail("antonio@email.com");
        funcionarioDTO.setNome("Antonio");
        funcionarioDTO.setFilial("Filial A");
        funcionarioDTO.setDepartamento("Departamento A");
        return funcionarioDTO;
    }

    private static FuncionarioDTO getFuncionarioDTO2() {
        FuncionarioDTO funcionarioDTO = new FuncionarioDTO();
        funcionarioDTO.setId("2");
        funcionarioDTO.setEmail("maria@email.com");
        funcionarioDTO.setNome("Maria");
        funcionarioDTO.setFilial("Filial B");
        funcionarioDTO.setDepartamento("Departamento B");
        return funcionarioDTO;
    }
}