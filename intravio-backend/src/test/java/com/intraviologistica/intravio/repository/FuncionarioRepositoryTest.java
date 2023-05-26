package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Funcionario;
import com.intraviologistica.intravio.model.FuncionarioTest;
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

    @Autowired
    private FilialRepository filialRepository;

    @Test
    public void testSalvarFuncionario() {
        Funcionario funcionario = FuncionarioTest.getFuncionario1();

        funcionarioRepository.save(funcionario);

        assertThat(funcionario.getId()).isNotNull();
    }

    @Test
    public void testBuscarFuncionarioPorEmail() {
        Funcionario funcionario = FuncionarioTest.getFuncionario1();
        funcionarioRepository.save(funcionario);

        Funcionario encontrado = funcionarioRepository.findByEmail("joao@teste.com");

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getId()).isEqualTo(funcionario.getId());
    }

    @Test
    public void testBuscarTodosFuncionarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();

        assertThat(funcionarios).isEmpty();

        Funcionario joao = FuncionarioTest.getFuncionario1();
        joao = funcionarioRepository.save(joao);

        Funcionario maria = FuncionarioTest.getFuncionario2();
        maria = funcionarioRepository.save(maria);

        funcionarios = funcionarioRepository.findAll();

        assertThat(funcionarios).hasSize(2);
        assertThat(funcionarios).contains(joao, maria);
    }

    @Test
    public void testBuscarFuncionarioPorId() {
        Funcionario funcionario = FuncionarioTest.getFuncionario1();
        funcionario = funcionarioRepository.save(funcionario);

        Funcionario encontrado = funcionarioRepository.findById(funcionario.getId()).orElse(null);

        assertThat(encontrado).isNotNull();
        assertThat(encontrado).isEqualTo(funcionario);
        assertThat(encontrado.getFilial()).isNull();
        assertThat(encontrado.getDepartamento()).isNull();
    }

    @Test
    public void testAtualizarFuncionario() {
        Funcionario funcionario = FuncionarioTest.getFuncionario1();

        funcionario = funcionarioRepository.save(funcionario);

        funcionario = new Funcionario(funcionario.getId(), "Maria Silva", "maria@email.com", null, funcionario.getFilial());

        Funcionario funcionarioAtualizado = funcionarioRepository.save(funcionario);

        assertThat(funcionarioAtualizado.getId()).isEqualTo(funcionario.getId());
        assertThat(funcionarioAtualizado.getNome()).isEqualTo(funcionario.getNome());
        assertThat(funcionarioAtualizado.getEmail()).isEqualTo(funcionario.getEmail());
        assertThat(funcionarioAtualizado.getDepartamento()).isEqualTo(funcionario.getDepartamento());
        assertThat(funcionarioAtualizado.getFilial()).isEqualTo(funcionario.getFilial());
    }

    @Test
    public void testExcluirFuncionario() {
        Funcionario funcionario = FuncionarioTest.getFuncionario1();

        funcionario = funcionarioRepository.save(funcionario);

        funcionarioRepository.deleteById(funcionario.getId());

        Optional<Funcionario> funcionarioRemovido = funcionarioRepository.findById(funcionario.getId());

        assertThat(funcionarioRemovido).isNotPresent();
    }
}