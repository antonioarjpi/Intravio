package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Endereco;
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

        Endereco endereco = new Endereco();
        endereco.setId("enderecoID2");
        endereco.setBairro("Bairro2");
        endereco.setRua("Rua2");
        endereco.setCep("CEP2");
        endereco.setNumero(1);
        endereco.setComplemento("Complemento2");
        endereco.setCidade("cidade2");
        endereco.setEstado("estado2");
        filialSalva.setEndereco(endereco);

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
        Endereco endereco = new Endereco();
        endereco.setId("enderecoID");
        endereco.setBairro("Bairro");
        endereco.setRua("Rua");
        endereco.setCep("CEP");
        endereco.setNumero(1);
        endereco.setComplemento("Complemento");
        endereco.setCidade("cidade");
        endereco.setEstado("estado");

        Filial filial = new Filial();
        filial.setId(1L);
        filial.setNome("Filial A");
        filial.setEndereco(endereco);
        return filial;
    }

    private static Filial getFilialB() {
        Endereco endereco = new Endereco();
        endereco.setId("enderecoID2");
        endereco.setBairro("Bairro2");
        endereco.setRua("Rua2");
        endereco.setCep("CEP2");
        endereco.setNumero(1);
        endereco.setComplemento("Complemento2");
        endereco.setCidade("cidade2");
        endereco.setEstado("estado2");

        Filial filialB = new Filial();
        filialB.setId(2L);
        filialB.setNome("Filial B");
        filialB.setEndereco(endereco);
        return filialB;
    }
}