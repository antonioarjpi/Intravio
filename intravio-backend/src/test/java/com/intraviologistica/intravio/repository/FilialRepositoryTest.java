package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Filial;
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
        Filial filial = getFilial();

        filialRepository.save(filial);

        assertThat(filial.getId()).isNotNull();
    }

    @Test
    public void testBuscarFilialPorNome() {
        Filial filial = getFilial();

        filialRepository.save(filial);

        Filial encontrado = filialRepository.findByNome("Filial A");

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getId()).isEqualTo(filial.getId());
    }

    @Test
    public void testBuscarTodasFiliais() {
        List<Filial> filiais = filialRepository.findAll();

        assertThat(filiais).isEmpty();

        Filial filialA = getFilial();
        filialA = filialRepository.save(filialA);

        Filial filialB = getFilialB();
        filialB = filialRepository.save(filialB);

        filiais = filialRepository.findAll();

        assertThat(filiais).hasSize(2);
        assertThat(filiais).contains(filialA, filialB);
    }

    @Test
    public void testAtualizarFilial() {
        Filial filialSalva = filialRepository.save(getFilial());

        filialSalva = new Filial(filialSalva.getId(), "Filial C");

        filialRepository.save(filialSalva);

        Optional<Filial> filialAtualizada = filialRepository.findById(filialSalva.getId());

        assertThat(filialAtualizada.isPresent()).isTrue();
        assertThat(filialAtualizada.get().getId()).isEqualTo(filialSalva.getId());
        assertThat(filialAtualizada.get().getNome()).isEqualTo(filialSalva.getNome());
    }

    @Test
    public void testExcluirFilial() {
        Filial filialSalva = filialRepository.save(getFilial());

        assertThat(filialSalva.getId()).isNotNull();

        filialRepository.deleteById(filialSalva.getId());

        Optional<Filial> filialExcluida = filialRepository.findById(filialSalva.getId());

        assertThat(filialExcluida.isPresent()).isFalse();
    }

    @Test
    public void testBuscarFilialPorId() {
        Filial filial = getFilial();
        filial = filialRepository.save(filial);

        Filial encontrado = filialRepository.findById(filial.getId()).orElse(null);

        assertThat(encontrado).isNotNull();
        assertThat(encontrado).isEqualTo(filial);
    }

    private static Filial getFilial() {
        Filial filial = new Filial();
        filial.setId(1L);
        filial.setNome("Filial A");
        return filial;
    }

    private static Filial getFilialB() {
        Filial filialB = new Filial();
        filialB.setId(2L);
        filialB.setNome("Filial B");
        return filialB;
    }
}