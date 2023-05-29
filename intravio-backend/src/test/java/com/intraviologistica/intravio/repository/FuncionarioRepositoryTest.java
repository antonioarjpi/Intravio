package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.*;
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

        Filial filial = filialRepository.save(FilialTest.getFilial1());
        Departamento departamento = departamentoRepository.save(DepartamentoTest.getDepartamento1());

        funcionario.setFilial(filial);
        funcionario.setDepartamento(departamento);

        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);

        assertThat(funcionarioSalvo.getId()).isNotNull();
        assertThat(funcionarioSalvo.getNome()).isEqualTo(funcionario.getNome());
        assertThat(funcionarioSalvo.getEmail()).isEqualTo(funcionario.getEmail());
        assertThat(funcionarioSalvo.getFilial()).isEqualTo(funcionario.getFilial());
        assertThat(funcionarioSalvo.getDepartamento()).isEqualTo(funcionario.getDepartamento());
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

        Filial filial = filialRepository.save(FilialTest.getFilial1());
        Departamento departamento = departamentoRepository.save(DepartamentoTest.getDepartamento1());

        Funcionario joao = FuncionarioTest.getFuncionario1();
        joao.setFilial(filial);
        joao = funcionarioRepository.save(joao);

        Funcionario maria = FuncionarioTest.getFuncionario2();
        maria.setDepartamento(departamento);
        maria = funcionarioRepository.save(maria);

        funcionarios = funcionarioRepository.findAll();

        assertThat(funcionarios).hasSize(2);
        assertThat(funcionarios).contains(joao, maria);
        assertThat(joao.getFilial()).isNotNull();
        assertThat(joao.getDepartamento()).isNull();
        assertThat(maria.getFilial()).isNull();
        assertThat(maria.getDepartamento()).isNotNull();
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