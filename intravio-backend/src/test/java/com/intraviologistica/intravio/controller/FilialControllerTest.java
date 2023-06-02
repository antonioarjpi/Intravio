package com.intraviologistica.intravio.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intraviologistica.intravio.dto.FilialDTO;
import com.intraviologistica.intravio.model.Endereco;
import com.intraviologistica.intravio.model.Filial;
import com.intraviologistica.intravio.service.FilialService;
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
public class FilialControllerTest {

    @InjectMocks
    private FilialController filialController;

    @Mock
    private FilialService filialService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(filialController).build();
    }

    @Test
    public void testListarFilials() throws Exception {
        FilialDTO filial1 = getFilial1();

        FilialDTO filial2 = getFilial2();

        List<FilialDTO> filials = Arrays.asList(filial1, filial2);

        // Mock do serviço
        Mockito.when(filialService.listarFiliais()).thenReturn(filials);

        // Executa a requisição GET
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filiais"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome", Matchers.is("Filial A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rua", Matchers.is("Rua Principal")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cep", Matchers.is("12345-678")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bairro", Matchers.is("Bairro Central")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].numero", Matchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].complemento", Matchers.is("Complemento A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].estado", Matchers.is("Estado A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cidade", Matchers.is("Cidade A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].nome", Matchers.is("Filial B")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].rua", Matchers.is("Rua Secundária")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].cep", Matchers.is("98765-432")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].bairro", Matchers.is("Bairro Secundário")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].numero", Matchers.is(200)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].complemento", Matchers.is("Complemento B")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].estado", Matchers.is("Estado B")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].cidade", Matchers.is("Cidade B")));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(filialService, Mockito.times(1)).listarFiliais();
        Mockito.verifyNoMoreInteractions(filialService);
    }

    @Test
    public void testBuscarFilialPorId() throws Exception {
        Long filialId = 1L;

        FilialDTO filialDTO = getFilial1();

        // Mock do serviço
        Mockito.when(filialService.buscarFilialPorId(filialId))
                .thenReturn(filialDTO);

        // Executa a requisição GET com o ID do filial e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/filiais/{id}", filialId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is("Filial A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rua", Matchers.is("Rua Principal")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cep", Matchers.is("12345-678")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bairro", Matchers.is("Bairro Central")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numero", Matchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.complemento", Matchers.is("Complemento A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.estado", Matchers.is("Estado A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cidade", Matchers.is("Cidade A")));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(filialService, Mockito.times(1))
                .buscarFilialPorId(filialId);
        Mockito.verifyNoMoreInteractions(filialService);
    }

    @Test
    public void testSalvarFilial() throws Exception {
        FilialDTO filialDTO = getFilial1();

        Filial filialSalvo = new Filial();
        filialSalvo.setId(filialDTO.getId());
        filialSalvo.setNome(filialDTO.getNome());
        filialSalvo.setEndereco(getEndereco());

        // Mock do serviço
        Mockito.when(filialService.salvaFilial(Mockito.any(FilialDTO.class)))
                .thenReturn(filialSalvo);

        // Executa a requisição POST com o filial e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/filiais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoToJson(filialDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is("Filial A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.rua", Matchers.is("Rua Principal")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.cep", Matchers.is("12345-678")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.bairro", Matchers.is("Bairro Central")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.numero", Matchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.complemento", Matchers.is("Complemento A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.estado", Matchers.is("Estado A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.cidade", Matchers.is("Cidade A")));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(filialService, Mockito.times(1))
                .salvaFilial(Mockito.any(FilialDTO.class));
        Mockito.verifyNoMoreInteractions(filialService);
    }

    @Test
    public void testAtualizarFilial() throws Exception {
        FilialDTO filialDTO = getFilial1();

        Filial filialSalvo = new Filial();
        filialSalvo.setId(filialDTO.getId());
        filialSalvo.setNome(filialDTO.getNome());
        filialSalvo.setEndereco(getEndereco());

        // Mock do serviço
        Mockito.when(filialService.atualizaFilial(Mockito.any(FilialDTO.class)))
                .thenReturn(filialSalvo);

        // Executa a requisição PUT com o ID da filial e a filial atualizado e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/filiais/{id}", filialDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoToJson(filialDTO)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is("Filial A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.rua", Matchers.is("Rua Principal")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.cep", Matchers.is("12345-678")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.bairro", Matchers.is("Bairro Central")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.numero", Matchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.complemento", Matchers.is("Complemento A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.estado", Matchers.is("Estado A")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endereco.cidade", Matchers.is("Cidade A")));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(filialService, Mockito.times(1)).atualizaFilial(Mockito.any(FilialDTO.class));
        Mockito.verifyNoMoreInteractions(filialService);
    }

    @Test
    public void testExcluirFilial() throws Exception {
        Long id = 1L;

        // Executa a requisição DELETE com o ID do filial e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/filiais/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(filialService, Mockito.times(1))
                .excluiFilial(id);
        Mockito.verifyNoMoreInteractions(filialService);
    }

    private static String converteObjetoToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private static Endereco getEndereco() {
        Endereco endereco = new Endereco();
        endereco.setId("1");
        endereco.setRua("Rua Principal");
        endereco.setCep("12345-678");
        endereco.setNumero(100);
        endereco.setBairro("Bairro Central");
        endereco.setEstado("Estado A");
        endereco.setCidade("Cidade A");
        endereco.setComplemento("Complemento A");
        return endereco;
    }

    private static FilialDTO getFilial1() {
        FilialDTO filialDTO = new FilialDTO();
        filialDTO.setId(1L);
        filialDTO.setNome("Filial A");
        filialDTO.setRua("Rua Principal");
        filialDTO.setCep("12345-678");
        filialDTO.setBairro("Bairro Central");
        filialDTO.setNumero(100);
        filialDTO.setComplemento("Complemento A");
        filialDTO.setEstado("Estado A");
        filialDTO.setCidade("Cidade A");
        return filialDTO;
    }

    private static FilialDTO getFilial2() {
        FilialDTO filialDTO = new FilialDTO();
        filialDTO.setId(2L);
        filialDTO.setNome("Filial B");
        filialDTO.setRua("Rua Secundária");
        filialDTO.setCep("98765-432");
        filialDTO.setBairro("Bairro Secundário");
        filialDTO.setNumero(200);
        filialDTO.setComplemento("Complemento B");
        filialDTO.setEstado("Estado B");
        filialDTO.setCidade("Cidade B");
        return filialDTO;
    }
}