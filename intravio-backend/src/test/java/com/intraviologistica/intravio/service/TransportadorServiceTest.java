package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.TransportadorDTO;
import com.intraviologistica.intravio.model.Transportador;
import com.intraviologistica.intravio.repository.TransportadorRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TransportadorServiceTest {

    @Mock
    private TransportadorRepository transportadorRepository;

    private TransportadorService transportadorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        transportadorService = new TransportadorService(transportadorRepository);

        Transportador transportador = getTransportador();

        List<Transportador> listaTransportadors = new ArrayList<>(List.of(transportador));

        when(transportadorRepository.findAll()).thenReturn(listaTransportadors); // Configurando o comportamento esperado do repositório mockado
        when(transportadorRepository.findById("1ab2c-42adc")).thenReturn(Optional.of(transportador)); // Configurando o comportamento esperado do repositório mockado
        when(transportadorRepository.save(any(Transportador.class))).thenReturn(getTransportador()); // Configurando o comportamento esperado do repositório mockado
    }

    @Test
    public void testListarTransportadors_QuandoSucesso() {
        Transportador transportador = getTransportador();
        List<TransportadorDTO> resultado = transportadorService.listaTodosTransportadores();

        assertNotNull(resultado); // Verificando se o resultado não é nulo
        assertEquals(1, resultado.size());  // Verificando se a quantidade de transportadors está correta

        // Verificando se a lista de transportadors está ordenada pelo nome
        assertEquals(transportador.getNome(), resultado.get(0).getNome());
        assertEquals(transportador.getId(), resultado.get(0).getId());
        assertEquals(transportador.getCnpj(), resultado.get(0).getCnpj());
        assertEquals(transportador.getMotorista(), resultado.get(0).getMotorista());
        assertEquals(transportador.getVeiculo(), resultado.get(0).getVeiculo());
        assertEquals(transportador.getObservacao(), resultado.get(0).getObservacao());
        assertEquals(transportador.getPlaca(), resultado.get(0).getPlaca());
    }

    @Test
    void test_buscarTransportadorPorId_QuandoEncontrar() {
        Transportador transportador = getTransportador();

        Transportador resultado = transportadorService.findById("1ab2c-42adc");

        assertNotNull(resultado);// Verificando se o resultado não é nulo

        // Verificando se o transportador retornado tem os mesmos valores do transportador de exemplo
        assertEquals(transportador.getId(), resultado.getId());
        assertEquals(transportador.getNome(), resultado.getNome());
    }

    @Test
    void test_buscarTransportadorPorId_QuandoNãoEncontrarTransportador() {
        String id = "naoExiste";

        when(transportadorRepository.findById(id)).thenReturn(Optional.empty()); //Configurando o comportamento receber valor vazio

        catchThrowableOfType(() -> transportadorService.findById(id), ResourceNotFoundException.class);
    }

    @Test
    void testBuscarTransportadorPorIdRetornandoDTO_QuandoEncontrar() {
        Transportador transportador = getTransportador();

        TransportadorDTO resultado = transportadorService.buscaTransportadorPorId("1ab2c-42adc");

        assertNotNull(resultado);// Verificando se o resultado não é nulo

        // Verificando se o transportador retornado tem os mesmos valores do transportador de exemplo
        assertEquals(transportador.getId(), resultado.getId());
        assertEquals(transportador.getNome(), resultado.getNome());
    }

    @Test
    void testBuscarTransportadorPorIdRetornandoDTO_QuandoNãoEncontrarTransportador() {
        String id = "naoExiste";

        when(transportadorRepository.findById(id)).thenReturn(Optional.empty()); //Configurando o comportamento receber valor vazio

        catchThrowableOfType(() -> transportadorService.buscaTransportadorPorId(id), ResourceNotFoundException.class);
    }

    @Test
    public void testSalvarTransportador_QuandoSucesso() {
        TransportadorDTO transportadorDTO = new TransportadorDTO();  // Criando um transportador de exemplo

        Transportador transportador = transportadorService.salvaTransportador(transportadorDTO);// Chamando o método a ser testado

        assertNotNull(transportador.getId());
        verify(transportadorRepository, times(1)).save(any(Transportador.class)); // Verificando se o método save() do repositório foi chamado uma vez
    }

    @Test
    public void testAtualizaTransportador_QuandoSucesso() {
        TransportadorDTO dto = getTransportadorInpuTDTO();

        Transportador transportadorRetornado = new Transportador(); // Cria um objeto Transportador simulado retornado pelo findById
        transportadorRetornado.setId(dto.getId());
        transportadorRetornado.setNome("Transportadora Antiga");
        transportadorRetornado.setCnpj("11.111.111/1111-11");
        transportadorRetornado.setMotorista("Antigo");
        transportadorRetornado.setPlaca("ABC-1234");
        transportadorRetornado.setVeiculo("Antigo Veículo");
        transportadorRetornado.setObservacao("Antiga Observacao");

        // Configura o comportamento do mock repository
        when(transportadorRepository.findById(dto.getId())).thenReturn(java.util.Optional.of(transportadorRetornado));
        when(transportadorRepository.save(any(Transportador.class))).thenReturn(transportadorRetornado);

        Transportador transportadorAtualizado = transportadorService.atualizaTransportador(dto);

        verify(transportadorRepository, times(1)).save(any(Transportador.class)); // Verificando se o método save() do repositório foi chamado uma vez

        // Verifica se os valores foram corretamente atualizados
        assertEquals(dto.getId(), transportadorAtualizado.getId());
        assertEquals(dto.getNome(), transportadorAtualizado.getNome());
        assertEquals(dto.getCnpj(), transportadorAtualizado.getCnpj());
        assertEquals(dto.getMotorista(), transportadorAtualizado.getMotorista());
        assertEquals(dto.getPlaca(), transportadorAtualizado.getPlaca());
        assertEquals(dto.getVeiculo(), transportadorAtualizado.getVeiculo());
        assertEquals(dto.getObservacao(), transportadorAtualizado.getObservacao());
    }

    @Test
    public void testDeletaTransportador_QuandoSucesso() {
        String id = "1ab2c-42adc";

        transportadorService.deletaTransportador(id);

        verify(transportadorRepository, times(1)).findById(id);  // Verificando se o método findById() foi chamado uma vez
        verify(transportadorRepository, times(1)).deleteById(id);  // Verificando se o método deleteById() foi chamado uma vez com o ID "1"
    }

    @Test
    void testExcluirTransportador_QuandoFalhar() {
        String id = "1abc";

        when(transportadorRepository.findById(anyString())).thenReturn(Optional.empty());

        catchThrowableOfType(() -> transportadorService.deletaTransportador(id), ResourceNotFoundException.class);

        verify(transportadorRepository, times(0)).deleteById(id); // Verificando se o método deleteById() foi chamado uma vez
    }


    @Test
    public void testToEntity() {
        TransportadorDTO dto = new TransportadorDTO();
        dto.setNome("Transportadora Teste");
        dto.setCnpj("00.000.000/0001-00");
        dto.setMotorista("Edvaldo");
        dto.setPlaca("PCX-1234");
        dto.setVeiculo("Mercedes-Benz");
        dto.setObservacao("Observacao");

        Transportador transportador = transportadorService.toEntity(dto);

        // Verifica se os valores foram corretamente mapeados
        assertEquals(dto.getNome(), transportador.getNome());
        assertEquals(dto.getCnpj(), transportador.getCnpj());
        assertEquals(dto.getMotorista(), transportador.getMotorista());
        assertEquals(dto.getPlaca(), transportador.getPlaca());
        assertEquals(dto.getVeiculo(), transportador.getVeiculo());
        assertEquals(dto.getObservacao(), transportador.getObservacao());
    }

    @Test
    public void testToDTO() {
        Transportador transportador = new Transportador();
        transportador.setId("1ab2c-42adc");
        transportador.setNome("Transportadora Teste");
        transportador.setCnpj("00.000.000/0001-00");
        transportador.setMotorista("Edvaldo");
        transportador.setPlaca("PCX-1234");
        transportador.setVeiculo("Mercedes-Benz");
        transportador.setObservacao("Observacao");

        TransportadorDTO dto = transportadorService.toDTO(transportador);

        // Verifica se os valores foram corretamente mapeados
        assertEquals(transportador.getId(), dto.getId());
        assertEquals(transportador.getNome(), dto.getNome());
        assertEquals(transportador.getCnpj(), dto.getCnpj());
        assertEquals(transportador.getMotorista(), dto.getMotorista());
        assertEquals(transportador.getPlaca(), dto.getPlaca());
        assertEquals(transportador.getVeiculo(), dto.getVeiculo());
        assertEquals(transportador.getObservacao(), dto.getObservacao());
    }

    public static Transportador getTransportador() {
        Transportador transportador = new Transportador();
        transportador.setId("1ab2c-42adc");
        transportador.setNome("Transportadora Teste");
        transportador.setCnpj("00.000.000/0001-00");
        transportador.setMotorista("Edvaldo");
        transportador.setPlaca("PCX-1234");
        transportador.setVeiculo("Mercedes-Benz");
        transportador.setObservacao("Observacao");
        return transportador;
    }

    public static TransportadorDTO getTransportadorInpuTDTO() {
        TransportadorDTO transportador = new TransportadorDTO();
        transportador.setId("1ab2c-42adc");
        transportador.setNome("Transportadora Teste");
        transportador.setCnpj("00.000.000/0001-00");
        transportador.setMotorista("Edvaldo");
        transportador.setPlaca("PCX-1234");
        transportador.setVeiculo("Mercedes-Benz");
        transportador.setObservacao("Observacao");
        return transportador;
    }
}