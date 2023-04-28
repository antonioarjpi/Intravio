package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Transportador;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TransportadorRepositoryTest {

    @Autowired
    private TransportadorRepository transportadorRepository;

    @Test
    public void testSalvarTransportador() {
        Transportador transportador = getTransportador();

        Transportador transportadorSalvo = transportadorRepository.save(transportador);

        assertThat(transportadorSalvo).isNotNull();
        assertThat(transportadorSalvo.getId()).isNotNull();
        assertThat(transportadorSalvo.getNome()).isEqualTo(transportador.getNome());
        assertThat(transportadorSalvo.getMotorista()).isEqualTo(transportador.getMotorista());
        assertThat(transportadorSalvo.getPlaca()).isEqualTo(transportador.getPlaca());
        assertThat(transportadorSalvo.getVeiculo()).isEqualTo(transportador.getVeiculo());
        assertThat(transportadorSalvo.getObservacao()).isEqualTo(transportador.getObservacao());
    }

    @Test
    public void testBuscarTransportadorPorId() {
        Transportador transportador = getTransportador();
        transportadorRepository.save(transportador);

        Optional<Transportador> transportadorEncontrado = transportadorRepository.findById(transportador.getId());
        assertThat(transportadorEncontrado.isPresent()).isTrue();
        assertThat(transportador.getNome()).isEqualTo(transportadorEncontrado.get().getNome());
        assertThat(transportador.getMotorista()).isEqualTo(transportadorEncontrado.get().getMotorista());
        assertThat(transportador.getPlaca()).isEqualTo(transportadorEncontrado.get().getPlaca());
        assertThat(transportador.getVeiculo()).isEqualTo(transportadorEncontrado.get().getVeiculo());
        assertThat(transportador.getObservacao()).isEqualTo(transportadorEncontrado.get().getObservacao());
    }

    @Test
    public void testBuscarTodosTransportadores() {
        Transportador transportador1 = getTransportador();
        transportador1 = transportadorRepository.save(transportador1);

        Transportador transportador2 = getTransportador();
        transportador2.setId("id2");
        transportador2.setNome("Nova Transportadora");
        transportador2.setMotorista("João");
        transportador2.setPlaca("XXX-2222");
        transportador2.setVeiculo("Fiat");
        transportador2.setObservacao("Sem observação");
        transportador2.setCnpj("00.000.000/0001-99");
        transportador2 = transportadorRepository.save(transportador2);

        List<Transportador> transportadores = transportadorRepository.findAll();
        assertThat(transportadores.size()).isEqualTo(2);
        assertThat(transportadores).contains(transportador1, transportador2);
    }

    @Test
    public void testAtualizarTransportador() {
        Transportador transportadorSalvo = transportadorRepository.save(getTransportador());

        transportadorSalvo = new Transportador(transportadorSalvo.getId(), "Transportador", "Eduardo", "III-0000", "Volks", "Nova Observacao", "99.999.999/0001-99");

        transportadorRepository.save(transportadorSalvo);

        Optional<Transportador> transportadorAtualizado = transportadorRepository.findById(transportadorSalvo.getId());

        assertThat(transportadorAtualizado).isPresent();
        assertThat(transportadorAtualizado.get().getNome()).isEqualTo(transportadorSalvo.getNome());
        assertThat(transportadorAtualizado.get().getMotorista()).isEqualTo(transportadorSalvo.getMotorista());
        assertThat(transportadorAtualizado.get().getPlaca()).isEqualTo(transportadorSalvo.getPlaca());
        assertThat(transportadorAtualizado.get().getVeiculo()).isEqualTo(transportadorSalvo.getVeiculo());
        assertThat(transportadorAtualizado.get().getCnpj()).isEqualTo(transportadorSalvo.getCnpj());
        assertThat(transportadorAtualizado.get().getObservacao()).isEqualTo(transportadorSalvo.getObservacao());
        assertThat(transportadorAtualizado.get().getId()).isEqualTo(transportadorSalvo.getId());
    }


    @Test
    public void testExcluirTransportador() {
        Transportador transportadorSalvo = transportadorRepository.save(getTransportador());

        transportadorRepository.deleteById(transportadorSalvo.getId());

        Optional<Transportador> transportadorExcluido = transportadorRepository.findById(transportadorSalvo.getId());
        assertThat(transportadorExcluido).isNotPresent();
    }

    private static Transportador getTransportador() {
        Transportador transportador = new Transportador();
        transportador.setId("id1");
        transportador.setNome("VIP Transportadora");
        transportador.setMotorista("Carlos");
        transportador.setPlaca("PPP-1111");
        transportador.setVeiculo("Mercedes-Benz");
        transportador.setObservacao("Observacao");
        transportador.setCnpj("00.000.000/0001-00");
        return transportador;
    }
}