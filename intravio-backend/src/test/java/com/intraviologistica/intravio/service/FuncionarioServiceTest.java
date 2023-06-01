package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.FuncionarioDTO;
import com.intraviologistica.intravio.dto.input.FuncionarioInputDTO;
import com.intraviologistica.intravio.model.Departamento;
import com.intraviologistica.intravio.model.Endereco;
import com.intraviologistica.intravio.model.Filial;
import com.intraviologistica.intravio.model.Funcionario;
import com.intraviologistica.intravio.repository.DepartamentoRepository;
import com.intraviologistica.intravio.repository.FilialRepository;
import com.intraviologistica.intravio.repository.FuncionarioRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private DepartamentoRepository departamentoRepository;

    @Mock
    private FilialRepository filialRepository;
    @InjectMocks
    private FuncionarioService funcionarioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Funcionario funcionario1 = getFuncionario1();
        Funcionario funcionario2 = getFuncionario2();

        List<Funcionario> funcionarios = new ArrayList<>(List.of(funcionario1, funcionario2));

        when(funcionarioRepository.findAll()).thenReturn(funcionarios);
        when(funcionarioRepository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.of(funcionario1));
        when(funcionarioRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(funcionario1);
        when(funcionarioRepository.save(ArgumentMatchers.any(Funcionario.class))).thenReturn(funcionario1);

        when(departamentoRepository.findById("123-abc")).thenReturn(Optional.of(getFuncionario1().getDepartamento()));
        when(filialRepository.findById(1L)).thenReturn(Optional.of(getFuncionario2().getFilial()));
    }

    @Test
    public void testListaFuncionarios() {
        List<FuncionarioDTO> result = funcionarioService.listaFuncionarios();

        assertEquals(2, result.size()); // Verifica se a lista possui tamanho 1

        FuncionarioDTO carlos = result.get(0);
        assertEquals("1234abcd", carlos.getId());
        assertEquals("Carlos", carlos.getNome());
        assertEquals("carlos@email.com", carlos.getEmail());
        assertEquals("Financeiro", carlos.getDepartamento());
        assertEquals("Alpha", carlos.getFilial());

        FuncionarioDTO maria = result.get(1);
        assertEquals("5678efgh", maria.getId());
        assertEquals("Maria", maria.getNome());
        assertEquals("maria@email.com", maria.getEmail());
        assertEquals("Recursos Humanos", maria.getDepartamento());
        assertEquals("Beta", maria.getFilial());
    }

    @Test
    public void testFindById_QuandoEncontraFuncionarioPorId() {
        Funcionario result = funcionarioService.findById("1234abcd");

        // Verificação do resultado esperado
        assertEquals("Carlos", result.getNome());
        assertEquals("1234abcd", result.getId());
        assertEquals("Carlos", result.getNome());
        assertEquals("carlos@email.com", result.getEmail());
        assertEquals("Financeiro", result.getDepartamento().getNome());
        assertEquals("Alpha", result.getFilial().getNome());
    }

    @Test
    public void testFindByIdNotFound_QuandoFalhar() {
        when(funcionarioRepository.findById("5678efgh")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> funcionarioService.findById("5678efgh")); // Execução do método que será testado e verificação da exceção lançada
    }

    @Test
    public void testBuscaFuncionarioPorEmail_QuandoEncontrarEmail() {
        Funcionario result = funcionarioService.buscaFuncionarioPorEmail("carlos@email.com");

        assertEquals("Carlos", result.getNome());
        assertEquals("1234abcd", result.getId());
        assertEquals("Carlos", result.getNome());
        assertEquals("carlos@email.com", result.getEmail());
        assertEquals("Financeiro", result.getDepartamento().getNome());
        assertEquals("Alpha", result.getFilial().getNome());
    }

    @Test
    public void testBuscaFuncionarioPorEmail_QuandoNaoEncontrar() {
        when(funcionarioRepository.findByEmail("email@inexistente.com")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> funcionarioService.buscaFuncionarioPorEmail("email@inexistente.com"));
    }

    @Test
    public void testBuscarFuncionarioPorId_QuandoEncontraFuncionarioPorId() {
        FuncionarioDTO result = funcionarioService.buscaFuncionarioPorId("1234abcd");

        assertEquals("Carlos", result.getNome());
        assertEquals("1234abcd", result.getId());
        assertEquals("Carlos", result.getNome());
        assertEquals("carlos@email.com", result.getEmail());
        assertEquals("Financeiro", result.getDepartamento());
        assertEquals("Alpha", result.getFilial());
    }

    @Test
    public void testBuscarFuncionarioPorId_QuandoNãoEncontrarIdDoFuncionario() {
        when(funcionarioRepository.findById("5678efgh")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> funcionarioService.findById("5678efgh")); // Execução do método que será testado e verificação da exceção lançada
    }

    @Test
    public void testSalvaFuncionario_QuandoDepartamentoEFilialSaoEncontrado() {
        // Criação de um objeto FuncionarioInputDTO fictício
        FuncionarioInputDTO funcionarioInputDTO = new FuncionarioInputDTO();
        funcionarioInputDTO.setNome("Maria");
        funcionarioInputDTO.setEmail("carlos@email.com");
        funcionarioInputDTO.setDepartamento("Financeiro");
        funcionarioInputDTO.setFilial(1L);

        FuncionarioDTO result = funcionarioService.salvaFuncionario(funcionarioInputDTO);

        assertNotNull(result.getId()); // Verifica se o ID do Funcionario não é nulo
        verify(funcionarioRepository, times(1)).save(any(Funcionario.class));
        assertEquals("Carlos", result.getNome()); // Verifica se o nome do Funcionario é "Carlos"
        assertEquals(funcionarioInputDTO.getEmail(), result.getEmail()); // Verifica se o e-mail do Funcionario está correto
        assertNotNull(result.getDepartamento());
        assertNotNull(result.getFilial());
    }

    @Test
    public void testSalvaFuncionario_QuandoDepartamentoEFilialNaoSaoEncontrados() {
        Funcionario funcionario = getFuncionario1();
        funcionario.setDepartamento(null);
        funcionario.setFilial(null);
        when(funcionarioRepository.save(ArgumentMatchers.any(Funcionario.class))).thenReturn(funcionario);

        FuncionarioInputDTO funcionarioInputDTO = new FuncionarioInputDTO();
        funcionarioInputDTO.setNome("Maria");
        funcionarioInputDTO.setEmail("carlos@email.com");

        FuncionarioDTO result = funcionarioService.salvaFuncionario(funcionarioInputDTO);

        assertNotNull(result.getId()); // Verifica se o ID do Funcionario não é nulo
        verify(funcionarioRepository, times(1)).save(any(Funcionario.class));
        assertEquals("Carlos", result.getNome()); // Verifica se o nome do Funcionario é "Carlos"
        assertEquals(funcionarioInputDTO.getEmail(), result.getEmail()); // Verifica se o e-mail do Funcionario está correto
        assertNull(result.getDepartamento()); // Verifica se departamento é nulo
        assertNull(result.getFilial()); // Verifica se Filial é nulo
    }

    @Test
    public void testAtualizaFuncionario_QuandoSucesso() {
        FuncionarioInputDTO funcionarioInputDTO = new FuncionarioInputDTO();
        funcionarioInputDTO.setNome("Jane Doe");
        funcionarioInputDTO.setEmail("jane.doe@example.com");
        funcionarioInputDTO.setDepartamento("Financeiro");
        funcionarioInputDTO.setFilial(1L);

        FuncionarioDTO updatedFuncionarioDTO = funcionarioService.atualizaFuncionario("1234abcd", funcionarioInputDTO); // Chamando o método a ser testado

        verify(funcionarioRepository, times(1)).findById(getFuncionario1().getId()); // Verificando se o método findById() foi chamado uma vez
        verify(funcionarioRepository, times(1)).save(any(Funcionario.class)); // Verificando se o método save() foi chamado uma vez com o objeto Funcionario atualizado
        assertNotNull(updatedFuncionarioDTO); //Verificando se funcionario foi atualizado
        assertNotNull(updatedFuncionarioDTO.getDepartamento()); // Verificando se Departamento não é nulo
        assertNotNull(updatedFuncionarioDTO.getFilial()); // Verificando se Filial não é nulo
    }

    @Test
    public void testDeletarFuncionario_QuandoSucesso() {
        String id = "1234abcd";

        funcionarioService.deletarFuncionario(id);

        verify(funcionarioRepository, times(1)).findById(id);  // Verificando se o método findById() foi chamado uma vez
        verify(funcionarioRepository, times(1)).deleteById(id);  // Verificando se o método deleteById() foi chamado uma vez com o ID "1"
    }

    @Test
    public void testDeletarFuncionario_FuncionarioNaoEncontrado() {
        when(funcionarioRepository.findById(anyString())).thenReturn(Optional.empty());

        String id = "1234abcd";

        catchThrowableOfType(() -> funcionarioService.deletarFuncionario(id), ResourceNotFoundException.class);

        verify(funcionarioRepository, times(0)).deleteById(id); // Verificando se o método deleteById() foi chamado uma vez
    }

    @Test
    public void testToDTO_NullDepartamentoAndFilial_QuandoDepartamentoEFilialForemNulos() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId("5678efgh");
        funcionario.setNome("Maria");
        funcionario.setEmail("maria@email.com");

        FuncionarioDTO result = new FuncionarioDTO(funcionario);

        assertNull(result.getDepartamento()); // Verifica se o campo Departamento do DTO é nulo
        assertNull(result.getFilial()); // Verifica se o campo Filial do DTO é nulo
    }

    @Test
    public void testToDTO() {
        Funcionario funcionario = getFuncionario1(); // Utilize a função getFuncionario() fornecida anteriormente

        FuncionarioDTO result = new FuncionarioDTO();
        result.setId(funcionario.getId());
        result.setDepartamento(funcionario.getDepartamento().getNome());
        result.setFilial(funcionario.getFilial().getNome());
        result.setEmail(funcionario.getEmail());
        result.setNome(funcionario.getNome());

        // Verificação do resultado esperado
        assertEquals("1234abcd", result.getId()); // Verifica se o ID do DTO é "1234abcd"
        assertEquals("Carlos", result.getNome()); // Verifica se o nome do DTO é "Carlos"
        assertEquals("carlos@email.com", result.getEmail()); // Verifica se o e-mail do DTO está correto
        assertEquals("Financeiro", result.getDepartamento()); // Verifica se o ID do departamento do DTO está correto
        assertEquals("Alpha", result.getFilial()); // Verifica se o ID da filial do DTO está correto
    }

    @Test
    public void testToEntity() {
        FuncionarioInputDTO funcionarioInputDTO = new FuncionarioInputDTO();
        funcionarioInputDTO.setId("1234abcd");
        funcionarioInputDTO.setNome("Carlos");
        funcionarioInputDTO.setEmail("carlos@email.com");

        Funcionario result = funcionarioService.toEntity(funcionarioInputDTO);

        // Verificação do resultado esperado
        assertEquals("1234abcd", result.getId()); // Verifica se o ID do Funcionario é "1234abcd"
        assertEquals("Carlos", result.getNome()); // Verifica se o nome do Funcionario é "Carlos"
        assertEquals(funcionarioInputDTO.getEmail(), result.getEmail()); // Verifica se o e-mail do Funcionario está correto
    }

    private static Funcionario getFuncionario1() {
        Endereco endereco = new Endereco();
        endereco.setId("11189");
        endereco.setBairro("Centro");
        endereco.setRua("Rua Principal");
        endereco.setCep("12345-678");
        endereco.setNumero(10);
        endereco.setComplemento("Sala 101");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");

        Filial filial = new Filial();
        filial.setId(1L);
        filial.setNome("Alpha");
        filial.setEndereco(endereco);

        Funcionario funcionario = new Funcionario();
        funcionario.setId("1234abcd");
        funcionario.setNome("Carlos");
        funcionario.setEmail("carlos@email.com");
        funcionario.setDepartamento(new Departamento("123-abc", "Financeiro"));
        funcionario.setFilial(filial);
        return funcionario;
    }

    private static Funcionario getFuncionario2() {
        Endereco endereco = new Endereco();
        endereco.setId("22222");
        endereco.setBairro("Bairro Novo");
        endereco.setRua("Avenida Principal");
        endereco.setCep("54321-876");
        endereco.setNumero(20);
        endereco.setComplemento("Sala 202");
        endereco.setCidade("Rio de Janeiro");
        endereco.setEstado("RJ");

        Filial filial = new Filial();
        filial.setId(2L);
        filial.setNome("Beta");
        filial.setEndereco(endereco);

        Funcionario funcionario = new Funcionario();
        funcionario.setId("5678efgh");
        funcionario.setNome("Maria");
        funcionario.setEmail("maria@email.com");
        funcionario.setDepartamento(new Departamento("456-def", "Recursos Humanos"));
        funcionario.setFilial(filial);
        return funcionario;
    }
}