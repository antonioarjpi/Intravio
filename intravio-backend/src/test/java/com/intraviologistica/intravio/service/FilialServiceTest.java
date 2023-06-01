package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.FilialDTO;
import com.intraviologistica.intravio.model.Filial;
import com.intraviologistica.intravio.model.FilialTest;
import com.intraviologistica.intravio.repository.FilialRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FilialServiceTest {

    @Mock
    private FilialRepository filialRepository;

    private FilialService filialService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        filialService = new FilialService(filialRepository);

        Filial filialAlpha = FilialTest.getFilial1();
        Filial filialBeta = FilialTest.getFilial2();

        List<Filial> listaFilials = new ArrayList<>(List.of(filialAlpha, filialBeta));

        when(filialRepository.findAll()).thenReturn(listaFilials); // Configurando o comportamento esperado do repositório mockado
        when(filialRepository.findById(1L)).thenReturn(Optional.of(filialAlpha)); // Configurando o comportamento esperado do repositório mockado
    }

    @Test
    public void testListarFilials_QuandoSucesso() {
        List<FilialDTO> resultado = filialService.listarFiliais();

        assertNotNull(resultado); // Verificando se o resultado não é nulo
        assertEquals(2, resultado.size());  // Verificando se a quantidade de filials está correta

        // Verificando se a lista de filials está ordenada pelo nome
        assertEquals("Filial Alpha", resultado.get(0).getNome());
        assertEquals("Filial Beta", resultado.get(1).getNome());
    }

    @Test
    void test_findById_QuandoEncontrarFilialPorId() {
        Filial resultado = filialService.findById(1L);

        assertNotNull(resultado);// Verificando se o resultado não é nulo

        // Verificando se a filial retornado tem os mesmos valores do filial de exemplo
        assertEquals(1L, resultado.getId());
        assertEquals("Filial Alpha", resultado.getNome());
        assertEquals("Centro", resultado.getEndereco().getBairro());
        assertEquals("Rua Principal", resultado.getEndereco().getRua());
        assertEquals("12345-678", resultado.getEndereco().getCep());
        assertEquals(10, resultado.getEndereco().getNumero());
        assertEquals("Sala 101", resultado.getEndereco().getComplemento());
        assertEquals("São Paulo", resultado.getEndereco().getCidade());
        assertEquals("SP", resultado.getEndereco().getEstado());
    }

    @Test
    void test_findById_QuandoNãoEncontrarFilialPorId() {
        Long id = 12L;

        when(filialRepository.findById(id)).thenReturn(Optional.empty()); //Configurando o comportamento receber valor vazio

        catchThrowableOfType(() -> filialService.findById(id), ResourceNotFoundException.class);
    }


    @Test
    void testBuscarFilialPorIdRetornandoDTO_QuandoEncontrar() {
        FilialDTO resultado = filialService.buscarFilialPorId(1L);

        assertNotNull(resultado);// Verificando se o resultado não é nulo

        // Verificando se o filial retornado tem os mesmos valores do filial de exemplo
        assertEquals(1L, resultado.getId());
        assertEquals("Filial Alpha", resultado.getNome());
        assertEquals("Centro", resultado.getBairro());
        assertEquals("Rua Principal", resultado.getRua());
        assertEquals("12345-678", resultado.getCep());
        assertEquals(10, resultado.getNumero());
        assertEquals("Sala 101", resultado.getComplemento());
        assertEquals("São Paulo", resultado.getCidade());
        assertEquals("SP", resultado.getEstado());
    }

    @Test
    void testBuscarFilialPorIdRetornandoDTO_QuandoNãoEncontrarFilial() {
        Long id = 12L;

        when(filialRepository.findById(id)).thenReturn(Optional.empty()); //Configurando o comportamento receber valor vazio

        catchThrowableOfType(() -> filialService.buscarFilialPorId(id), ResourceNotFoundException.class);
    }

    @Test
    public void testSalvarFilial_QuandoSucesso() {
        FilialDTO filialDTO = new FilialDTO();  // Criando um filial de exemplo

        filialService.salvaFilial(filialDTO); // Chamando o método a ser testado

        verify(filialRepository, times(1)).save(any(Filial.class)); // Verificando se o método save() do repositório foi chamado uma vez
    }

    @Test
    public void testAtualizarFilial_QuandoSucesso() {
        FilialDTO filialAtualizadoDTO = getFilialDTO(); // Criando um DTO de filial atualizado

        filialService.atualizaFilial(filialAtualizadoDTO); // Chamando o método a ser testado

        verify(filialRepository, times(1)).save(any(Filial.class)); // Verificando se o método save() do repositório foi chamado uma vez
    }

    @Test
    void testExcluirFilial_QuandoSucesso() {
        Long id = 1L;
        filialService.excluiFilial(id);

        verify(filialRepository, times(1)).findById(id); // Verificando se o método findById() foi chamado uma vez

        verify(filialRepository, times(1)).deleteById(id);   // Verificando se o método deleteById() foi chamado uma vez
    }

    @Test
    void testExcluirFilial_QuandoFalhar() {
        Long id = 1L;

        when(filialRepository.findById(anyLong())).thenReturn(Optional.empty());

        catchThrowableOfType(() -> filialService.excluiFilial(id), ResourceNotFoundException.class);

        verify(filialRepository, times(0)).deleteById(id); // Verificando se o método deleteById() foi chamado uma vez
    }

    @Test
    void toEntity() {
        FilialDTO filial = getFilialDTO();

        Filial resultado = filialService.toEntity(filial);

        assertThat(resultado).isNotNull(); //Verifica se resultado não é nulo
        assertEquals(1L, resultado.getId());
        assertEquals("Filial A", resultado.getNome());
        assertEquals("Rua Joca Vieira", resultado.getEndereco().getRua());
        assertEquals(2030, resultado.getEndereco().getNumero());
        assertEquals("Ininga", resultado.getEndereco().getBairro());
        assertEquals("Piauí", resultado.getEndereco().getEstado());
        assertEquals("Oeiras", resultado.getEndereco().getCidade());
        assertEquals("65000-000", resultado.getEndereco().getCep());
        assertEquals("Próximo a rede Globo", resultado.getEndereco().getComplemento());
    }

    @Test
    void testToDTO() {
        Filial filial = FilialTest.getFilial1();
        FilialDTO resultado = filialService.toDTO(filial);

        assertThat(resultado).isNotNull(); //Verifica se resultado não é nulo
        assertEquals(1L, resultado.getId());
        assertEquals("Filial Alpha", resultado.getNome());
        assertEquals("Centro", resultado.getBairro());
        assertEquals("Rua Principal", resultado.getRua());
        assertEquals("12345-678", resultado.getCep());
        assertEquals(10, resultado.getNumero());
        assertEquals("Sala 101", resultado.getComplemento());
        assertEquals("São Paulo", resultado.getCidade());
        assertEquals("SP", resultado.getEstado());
    }

    private static FilialDTO getFilialDTO() {
        FilialDTO filial = new FilialDTO();
        filial.setId(1L);
        filial.setNome("Filial A");
        filial.setRua("Rua Joca Vieira");
        filial.setBairro("Ininga");
        filial.setEstado("Piauí");
        filial.setCidade("Oeiras");
        filial.setCep("65000-000");
        filial.setComplemento("Próximo a rede Globo");
        filial.setNumero(2030);
        return filial;
    }
}