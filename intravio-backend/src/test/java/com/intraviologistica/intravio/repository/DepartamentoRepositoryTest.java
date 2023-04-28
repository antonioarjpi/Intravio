package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Departamento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class DepartamentoRepositoryTest {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Test
    void findByNomeIgnoreCase() {
    }

    @Test
    public void testSalvarDepartamento() {
        Departamento departamento = getDepartamento();

        departamentoRepository.save(departamento);

        assertThat(departamento.getId()).isNotNull();
    }

    @Test
    public void testBuscarDepartamentoPorNome() {
        Departamento departamento = departamentoRepository.save(getDepartamento());

        Departamento encontrado = departamentoRepository.findByNomeIgnoreCase("Departamento A");

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getId()).isEqualTo(departamento.getId());
    }

    @Test
    public void testBuscarTodosDepartamentos() {
        List<Departamento> departamentos = departamentoRepository.findAll();

        assertThat(departamentos).isEmpty();

        Departamento departamentoA = getDepartamento();
        departamentoA.setNome("Departamento A");
        departamentoA = departamentoRepository.save(departamentoA);

        Departamento departamentoB = new Departamento("id2", "Departamento B");
        departamentoB = departamentoRepository.save(departamentoB);

        departamentos = departamentoRepository.findAll();

        assertThat(departamentos).hasSize(2);
        assertThat(departamentos).contains(departamentoA, departamentoB);
    }

    @Test
    public void testBuscarDepartamentoPorId() {
        Departamento departamento = getDepartamento();
        departamento = departamentoRepository.save(departamento);

        Departamento encontrado = departamentoRepository.findById(departamento.getId()).orElse(null);

        assertThat(encontrado).isNotNull();
        assertThat(encontrado).isEqualTo(departamento);
    }

    @Test
    public void testAtualizarDepartamento() {
        Departamento departamentoSalvo = departamentoRepository.save(getDepartamento());

        departamentoSalvo = new Departamento(departamentoSalvo.getId(), "Novo Departamento");

        departamentoRepository.save(departamentoSalvo);

        Optional<Departamento> departamentoAtualizadoOpt = departamentoRepository.findById(departamentoSalvo.getId());

        assertThat(departamentoAtualizadoOpt).isPresent();
        Departamento departamentoAtualizado = departamentoAtualizadoOpt.get();
        assertThat(departamentoAtualizado.getId()).isEqualTo(departamentoSalvo.getId());
        assertThat(departamentoAtualizado.getNome()).isEqualTo(departamentoSalvo.getNome());
    }

    @Test
    public void testExcluirDepartamento() {
        Departamento departamentoSalvo = departamentoRepository.save(getDepartamento());

        departamentoRepository.deleteById(departamentoSalvo.getId());

        Optional<Departamento> departamentoExcluido = departamentoRepository.findById(departamentoSalvo.getId());

        assertThat(departamentoExcluido).isNotPresent();
    }

    private static Departamento getDepartamento() {
        Departamento departamento = new Departamento();
        departamento.setId("id1");
        departamento.setNome("Departamento A");
        return departamento;
    }
}