package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Departamento;
import com.intraviologistica.intravio.model.DepartamentoTest;
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
        Departamento departamento = DepartamentoTest.getDepartamento1();

        departamentoRepository.save(departamento);

        assertThat(departamento.getId()).isNotNull();
    }

    @Test
    public void testBuscarDepartamentoPorNome() {
        Departamento departamento = departamentoRepository.save(DepartamentoTest.getDepartamento1());

        Departamento encontrado = departamentoRepository.findByNomeIgnoreCase("Financeiro");

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getId()).isEqualTo(departamento.getId());
    }

    @Test
    public void testBuscarTodosDepartamentos() {
        List<Departamento> departamentos = departamentoRepository.findAll();

        assertThat(departamentos).isEmpty();

        Departamento departamentoA = departamentoRepository.save(DepartamentoTest.getDepartamento1());
        Departamento departamentoB = departamentoRepository.save(DepartamentoTest.getDepartamento2());

        departamentos = departamentoRepository.findAll();

        assertThat(departamentos).hasSize(2);
        assertThat(departamentos).contains(departamentoA, departamentoB);
    }

    @Test
    public void testBuscarDepartamentoPorId() {
        Departamento departamento = DepartamentoTest.getDepartamento1();
        departamento = departamentoRepository.save(departamento);

        Departamento encontrado = departamentoRepository.findById(departamento.getId()).orElse(null);

        assertThat(encontrado).isNotNull();
        assertThat(encontrado).isEqualTo(departamento);
        assertThat(encontrado.getId()).isEqualTo(departamento.getId());
        assertThat(encontrado.getNome()).isEqualTo(departamento.getNome());
    }

    @Test
    public void testAtualizarDepartamento() {
        Departamento departamentoSalvo = departamentoRepository.save(DepartamentoTest.getDepartamento1());

        departamentoSalvo = new Departamento(departamentoSalvo.getId(), "Marketing");

        departamentoRepository.save(departamentoSalvo);

        Optional<Departamento> departamentoAtualizadoOpt = departamentoRepository.findById(departamentoSalvo.getId());

        assertThat(departamentoAtualizadoOpt).isPresent();
        Departamento departamentoAtualizado = departamentoAtualizadoOpt.get();
        assertThat(departamentoAtualizado.getId()).isEqualTo(departamentoSalvo.getId());
        assertThat(departamentoAtualizado.getNome()).isEqualTo(departamentoSalvo.getNome());
    }

    @Test
    public void testExcluirDepartamento() {
        Departamento departamentoSalvo = departamentoRepository.save(DepartamentoTest.getDepartamento1());

        departamentoRepository.deleteById(departamentoSalvo.getId());

        Optional<Departamento> departamentoExcluido = departamentoRepository.findById(departamentoSalvo.getId());

        assertThat(departamentoExcluido).isNotPresent();
    }
}