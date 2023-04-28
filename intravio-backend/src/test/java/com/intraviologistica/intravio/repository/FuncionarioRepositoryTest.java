package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Departamento;
import com.intraviologistica.intravio.model.Funcionario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class FuncionarioRepositoryTest {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Test
    public void testSalvarFuncionario() {
        Departamento departamento = getDepartamento();

        Funcionario funcionario = getFuncionario(departamento);

        funcionarioRepository.save(funcionario);

        assertThat(funcionario.getId()).isNotNull();
    }

    @Test
    public void testBuscarFuncionarioPorEmail() {
        Departamento departamento = getDepartamento();

        Funcionario funcionario = getFuncionario(departamento);
        funcionarioRepository.save(funcionario);

        Funcionario encontrado = funcionarioRepository.findByEmail("joao@teste.com");

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getId()).isEqualTo(funcionario.getId());
    }

    @Test
    public void testBuscarTodosFuncionarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();

        assertThat(funcionarios).isEmpty();

        Departamento departamento = getDepartamento();

        Funcionario joao = getFuncionario(departamento);
        joao = funcionarioRepository.save(joao);

        Funcionario maria = getFuncionarioB(departamento);
        maria = funcionarioRepository.save(maria);

        funcionarios = funcionarioRepository.findAll();

        assertThat(funcionarios).hasSize(2);
        assertThat(funcionarios).contains(joao, maria);
    }

    @Test
    public void testBuscarFuncionarioPorId() {
        Departamento departamento = getDepartamento();

        Funcionario funcionario = getFuncionario(departamento);
        funcionario = funcionarioRepository.save(funcionario);

        Funcionario encontrado = funcionarioRepository.findById(funcionario.getId()).orElse(null);

        assertThat(encontrado).isNotNull();
        assertThat(encontrado).isEqualTo(funcionario);
    }

    @Test
    public void testAtualizarFuncionario() {
        Departamento departamento = getDepartamento();
        Funcionario funcionario = getFuncionario(departamento);

        funcionario = funcionarioRepository.save(funcionario);

        funcionario = new Funcionario(funcionario.getId(), "Novo Nome", "email@email.com", departamento);

        Funcionario funcionarioAtualizado = funcionarioRepository.save(funcionario);

        assertThat(funcionarioAtualizado.getId()).isEqualTo(funcionario.getId());
        assertThat(funcionarioAtualizado.getNome()).isEqualTo(funcionario.getNome());
        assertThat(funcionarioAtualizado.getEmail()).isEqualTo(funcionario.getEmail());
        assertThat(funcionarioAtualizado.getDepartamento()).isEqualTo(funcionario.getDepartamento());
    }

    @Test
    public void testExcluirFuncionario() {
        Departamento departamento = getDepartamento();
        Funcionario funcionario = getFuncionario(departamento);

        funcionario = funcionarioRepository.save(funcionario);

        funcionarioRepository.deleteById(funcionario.getId());

        Optional<Funcionario> funcionarioRemovido = funcionarioRepository.findById(funcionario.getId());

        assertThat(funcionarioRemovido).isNotPresent();
    }

    private static Funcionario getFuncionario(Departamento departamento) {
        Funcionario funcionario = new Funcionario();
        funcionario.setId("id1");
        funcionario.setNome("João");
        funcionario.setEmail("joao@teste.com");
        funcionario.setDepartamento(departamento);
        return funcionario;
    }

    private static Funcionario getFuncionarioB(Departamento departamento) {
        Funcionario maria = new Funcionario();
        maria.setId("id2");
        maria.setNome("Maria");
        maria.setEmail("maria@teste.com");
        maria.setDepartamento(departamento);
        return maria;
    }

    private Departamento getDepartamento() {
        Departamento departamento = new Departamento();
        departamento.setId("id-3");
        departamento.setNome("TI");
        departamento = departamentoRepository.save(departamento);
        return departamento;
    }
}