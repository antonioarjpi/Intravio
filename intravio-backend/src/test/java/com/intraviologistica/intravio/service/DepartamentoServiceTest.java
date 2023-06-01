package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.DepartamentoDTO;
import com.intraviologistica.intravio.model.Departamento;
import com.intraviologistica.intravio.model.DepartamentoTest;
import com.intraviologistica.intravio.repository.DepartamentoRepository;
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
import static org.mockito.Mockito.*;

class DepartamentoServiceTest {

    @Mock
    private DepartamentoRepository departamentoRepository;

    private DepartamentoService departamentoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        departamentoService = new DepartamentoService(departamentoRepository);

        Departamento financeiro = DepartamentoTest.getDepartamento1();
        Departamento comercial = DepartamentoTest.getDepartamento2();

        List<Departamento> listaDepartamentos = new ArrayList<>(List.of(financeiro, comercial));

        when(departamentoRepository.findAll()).thenReturn(listaDepartamentos); // Configurando o comportamento esperado do repositório mockado
        when(departamentoRepository.findById("141aba2")).thenReturn(Optional.of(financeiro)); // Configurando o comportamento esperado do repositório mockado
    }

    @Test
    public void testListarDepartamentos_QuandoSucesso() {
        List<DepartamentoDTO> resultado = departamentoService.listarDepartamentos();

        assertNotNull(resultado); // Verificando se o resultado não é nulo
        assertEquals(2, resultado.size());  // Verificando se a quantidade de departamentos está correta

        // Verificando se a lista de departamentos está ordenada pelo nome
        assertEquals("Comercial", resultado.get(0).getNome());
        assertEquals("Financeiro", resultado.get(1).getNome());
    }

    @Test
    void test_buscarDepartamentoPorId_QuandoEncontrar() {
        Departamento resultado = departamentoService.buscarDepartamentoPorId("141aba2");

        assertNotNull(resultado);// Verificando se o resultado não é nulo

        // Verificando se o departamento retornado tem os mesmos valores do departamento de exemplo
        assertEquals("141aba2", resultado.getId());
        assertEquals("Financeiro", resultado.getNome());
    }

    @Test
    void test_buscarDepartamentoPorId_QuandoNãoEncontrarDepartamento() {
        String id = "naoExiste";

        when(departamentoRepository.findById(id)).thenReturn(Optional.empty()); //Configurando o comportamento receber valor vazio

        catchThrowableOfType(() -> departamentoService.buscarDepartamentoPorId(id), ResourceNotFoundException.class);
    }


    @Test
    void testBuscarDepartamentoPorIdRetornandoDTO_QuandoEncontrar() {
        DepartamentoDTO resultado = departamentoService.buscarDepartamentoPorIdRetornandoDTO("141aba2");

        assertNotNull(resultado);// Verificando se o resultado não é nulo

        // Verificando se o departamento retornado tem os mesmos valores do departamento de exemplo
        assertEquals("141aba2", resultado.getId());
        assertEquals("Financeiro", resultado.getNome());
    }

    @Test
    void testBuscarDepartamentoPorIdRetornandoDTO_QuandoNãoEncontrarDepartamento() {
        String id = "naoExiste";

        when(departamentoRepository.findById(id)).thenReturn(Optional.empty()); //Configurando o comportamento receber valor vazio

        catchThrowableOfType(() -> departamentoService.buscarDepartamentoPorIdRetornandoDTO(id), ResourceNotFoundException.class);
    }

    @Test
    public void testSalvarDepartamento_QuandoSucesso() {
        DepartamentoDTO departamentoDTO = new DepartamentoDTO(null, "Financeiro");  // Criando um departamento de exemplo

        departamentoService.salvarDepartamento(departamentoDTO); // Chamando o método a ser testado

        verify(departamentoRepository, times(1)).save(any(Departamento.class)); // Verificando se o método save() do repositório foi chamado uma vez
    }

    @Test
    public void testAtualizarDepartamento_QuandoSucesso() {
        Departamento departamento = DepartamentoTest.getDepartamento1(); // Criando um departamento de exemplo

        DepartamentoDTO departamentoAtualizadoDTO = new DepartamentoDTO(null, "Financeiro"); // Criando um DTO de departamento atualizado

        when(departamentoRepository.findById("141aba2")).thenReturn(Optional.of(departamento)); // Configurando o comportamento esperado do método buscarDepartamentoPorId()

        departamentoService.atualizarDepartamento("141aba2", departamentoAtualizadoDTO); // Chamando o método a ser testado

        verify(departamentoRepository, times(1)).save(any(Departamento.class)); // Verificando se o método save() do repositório foi chamado uma vez
    }

    @Test
    void testExcluirDepartamento_QuandoSucesso() {
        String id = "141aba2";
        departamentoService.excluirDepartamento(id);

        verify(departamentoRepository, times(1)).findById(id); // Verificando se o método findById() foi chamado uma vez

        verify(departamentoRepository, times(1)).deleteById(id);  // Verificando se o método deleteById() foi chamado uma vez
    }

    @Test
    void testExcluirDepartamento_QuandoFalhar() {
        String id = "141aba2";

        when(departamentoRepository.findById(anyString())).thenReturn(Optional.empty());

        catchThrowableOfType(() -> departamentoService.excluirDepartamento(id), ResourceNotFoundException.class);

        verify(departamentoRepository, times(0)).deleteById(id); // Verificando se o método deleteById() foi chamado uma vez
    }

    @Test
    void toEntity() {
        DepartamentoDTO departamento = new DepartamentoDTO("141aba2", "Financeiro");
        Departamento resultado = departamentoService.toEntity(departamento);

        assertThat(resultado).isNotNull(); //Verifica se resultado não é nulo
        assertEquals("141aba2", resultado.getId());
        assertEquals("Financeiro", resultado.getNome());
    }

    @Test
    void testToDTO() {
        Departamento departamento = DepartamentoTest.getDepartamento1();
        DepartamentoDTO resultado = departamentoService.toDTO(departamento);

        assertThat(resultado).isNotNull(); //Verifica se resultado não é nulo
        assertEquals("141aba2", resultado.getId());
        assertEquals("Financeiro", resultado.getNome());
    }
}