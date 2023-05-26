package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Filial;
import com.intraviologistica.intravio.model.FilialTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class FilialRepositoryTest {

    @Autowired
    private FilialRepository filialRepository;

    @Test
    public void testSalvarFilial() {
        Filial filial = FilialTest.getFilial1();

        filialRepository.save(filial);

        assertThat(filial.getId()).isNotNull();
    }

    @Test
    public void testBuscarFilialPorNome() {
        Filial filial = FilialTest.getFilial1();

        filialRepository.save(filial);

        Filial encontrado = filialRepository.findByNome("Filial Alpha");

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getId()).isEqualTo(filial.getId());
    }

    @Test
    public void testBuscarTodasFiliais() {
        List<Filial> filiais = filialRepository.findAll();

        assertThat(filiais).isEmpty();

        Filial filialA = FilialTest.getFilial1();
        filialA = filialRepository.save(filialA);

        Filial filialB = FilialTest.getFilial2();
        filialB = filialRepository.save(filialB);

        filiais = filialRepository.findAll();

        assertThat(filiais).hasSize(2);
        assertThat(filiais).contains(filialA, filialB);
    }

    @Test
    public void testAtualizarFilial() {
        Filial filialSalva = filialRepository.save(FilialTest.getFilial1());

        filialSalva = FilialTest.getFilial2();

        filialRepository.save(filialSalva);

        Optional<Filial> filialAtualizada = filialRepository.findById(filialSalva.getId());

        assertThat(filialAtualizada.isPresent()).isTrue();
        assertThat(filialAtualizada.get().getId()).isEqualTo(filialSalva.getId());
        assertThat(filialAtualizada.get().getNome()).isEqualTo(filialSalva.getNome());
        assertThat(filialAtualizada.get().getEndereco().getId()).isEqualTo(filialSalva.getEndereco().getId());
        assertThat(filialAtualizada.get().getEndereco().getRua()).isEqualTo(filialSalva.getEndereco().getRua());
        assertThat(filialAtualizada.get().getEndereco().getBairro()).isEqualTo(filialSalva.getEndereco().getBairro());
        assertThat(filialAtualizada.get().getEndereco().getComplemento()).isEqualTo(filialSalva.getEndereco().getComplemento());
        assertThat(filialAtualizada.get().getEndereco().getCep()).isEqualTo(filialSalva.getEndereco().getCep());
        assertThat(filialAtualizada.get().getEndereco().getCidade()).isEqualTo(filialSalva.getEndereco().getCidade());
        assertThat(filialAtualizada.get().getEndereco().getEstado()).isEqualTo(filialSalva.getEndereco().getEstado());
        assertThat(filialAtualizada.get().getEndereco().getNumero()).isEqualTo(filialSalva.getEndereco().getNumero());
    }

    @Test
    public void testExcluirFilial() {
        Filial filialSalva = filialRepository.save(FilialTest.getFilial1());

        assertThat(filialSalva.getId()).isNotNull();

        filialRepository.deleteById(filialSalva.getId());

        Optional<Filial> filialExcluida = filialRepository.findById(filialSalva.getId());

        assertThat(filialExcluida.isPresent()).isFalse();
    }

    @Test
    public void testBuscarFilialPorId() {
        Filial filial = FilialTest.getFilial1();
        filial = filialRepository.save(filial);

        Filial encontrado = filialRepository.findById(filial.getId()).orElse(null);

        assertThat(encontrado).isNotNull();
        assertThat(encontrado).isEqualTo(filial);
        assertThat(encontrado.getId()).isEqualTo(filial.getId());
        assertThat(encontrado.getNome()).isEqualTo(filial.getNome());
        assertThat(encontrado.getEndereco().getRua()).isEqualTo(filial.getEndereco().getRua());
        assertThat(encontrado.getEndereco().getCep()).isEqualTo(filial.getEndereco().getCep());
        assertThat(encontrado.getEndereco().getBairro()).isEqualTo(filial.getEndereco().getBairro());
        assertThat(encontrado.getEndereco().getCidade()).isEqualTo(filial.getEndereco().getCidade());
        assertThat(encontrado.getEndereco().getEstado()).isEqualTo(filial.getEndereco().getEstado());
        assertThat(encontrado.getEndereco().getComplemento()).isEqualTo(filial.getEndereco().getComplemento());
        assertThat(encontrado.getEndereco().getNumero()).isEqualTo(filial.getEndereco().getNumero());
        assertThat(encontrado.getEndereco().getId()).isEqualTo(filial.getEndereco().getId());
    }

}