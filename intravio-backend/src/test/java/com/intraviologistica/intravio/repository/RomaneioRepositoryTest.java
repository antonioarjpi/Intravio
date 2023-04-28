package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.*;
import com.intraviologistica.intravio.model.enums.AcompanhaStatus;
import com.intraviologistica.intravio.model.enums.Prioridade;
import com.intraviologistica.intravio.model.enums.StatusPedido;
import com.intraviologistica.intravio.model.enums.StatusRomaneio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RomaneioRepositoryTest {

    @Autowired
    private RomaneioRepository romaneioRepository;

    @Autowired
    private TransportadorRepository transportadorRepository;

    @Test
    public void testSalvarRomaneio() {
        Romaneio romaneio = getRomaneio();

        Romaneio romaneioSalvo = romaneioRepository.save(romaneio);

        assertThat(romaneioSalvo.getId()).isNotNull();
        assertThat(romaneioSalvo.getStatus()).isEqualTo(StatusRomaneio.ABERTO);
        assertThat(romaneioSalvo.getPedidos()).isEmpty();
        assertThat(romaneioSalvo.getTaxaFrete()).isEqualTo(100.0);
    }

    @Test
    public void testBuscarRomaneioPorId() {
        Romaneio romaneioSalvo = romaneioRepository.save(getRomaneio());

        Optional<Romaneio> romaneioEncontrado = romaneioRepository.findById(romaneioSalvo.getId());

        assertThat(romaneioEncontrado).isPresent();
        assertThat(romaneioEncontrado.get().getId()).isEqualTo(romaneioSalvo.getId());
        assertThat(romaneioEncontrado.get().getTaxaFrete()).isEqualTo(100.0);
        assertThat(romaneioEncontrado.get().getStatus()).isEqualTo(StatusRomaneio.ABERTO);
    }

    @Test
    public void testAtualizarRomaneio() {
        Romaneio romaneio = getRomaneio();
        romaneio.setId(null);
        romaneio = romaneioRepository.save(getRomaneio());

        romaneio = new Romaneio(romaneio.getId(), null, getRomaneio().getTransportador(), StatusRomaneio.EM_TRANSITO, 15.00, romaneio.getDataCriacao(), LocalDateTime.now(), "Nova Observacao");

        romaneioRepository.save(romaneio);

        Optional<Romaneio> romaneioAtualizado = romaneioRepository.findById(romaneio.getId());

        assertThat(romaneioAtualizado).isPresent();
        assertThat(romaneioAtualizado.get().getId()).isEqualTo(romaneio.getId());
        assertThat(romaneioAtualizado.get().getStatus()).isEqualTo(StatusRomaneio.EM_TRANSITO);
        assertThat(romaneioAtualizado.get().getDataAtualizacao()).isEqualTo(romaneio.getDataAtualizacao());
        assertThat(romaneioAtualizado.get().getTaxaFrete()).isEqualTo(romaneio.getTaxaFrete());
        assertThat(romaneioAtualizado.get().getObservacao()).isEqualTo(romaneio.getObservacao());
        assertThat(romaneioAtualizado.get().getPedidos()).isEqualTo(romaneio.getPedidos());
    }

    @Test
    public void testBuscarTodosRomaneios() {
        Transportador transportador = transportadorRepository.save(getRomaneio().getTransportador());

        Romaneio romaneio1 = getRomaneio();
        romaneio1.setTransportador(transportador);
        romaneioRepository.save(romaneio1);

        Romaneio romaneio2 = new Romaneio();
        romaneio2.setTransportador(transportador);
        romaneio2.setStatus(StatusRomaneio.ABERTO);
        romaneio2.setTaxaFrete(80.0);
        romaneio2.setDataCriacao(LocalDateTime.now());
        romaneio2.setDataAtualizacao(LocalDateTime.now());
        romaneio2.setObservacao("Romaneio teste 2");
        romaneioRepository.save(romaneio2);

        List<Romaneio> romaneios = romaneioRepository.findAll();
        assertThat(romaneios).isNotNull();
        assertThat(romaneios).hasSize(2);
    }

    @Test
    public void testExcluirRomaneioPorId() {
        Romaneio romaneio = getRomaneio();
        romaneio = romaneioRepository.save(romaneio);

        Long id = romaneio.getId();

        romaneioRepository.deleteById(id);

        Optional<Romaneio> romaneioExcluido = romaneioRepository.findById(id);
        assertThat(romaneioExcluido.isPresent()).isFalse();
    }

    private static Romaneio getRomaneio() {
        Romaneio romaneio = new Romaneio();
        romaneio.setDataCriacao(LocalDateTime.now());
        romaneio.setDataAtualizacao(LocalDateTime.now());
        romaneio.setTaxaFrete(100.0);
        romaneio.setStatus(StatusRomaneio.ABERTO);
        romaneio.setObservacao("Romaneio teste");

        Transportador transportador = new Transportador();
        transportador.setNome("Transportadora Teste");
        transportador.setCnpj("1234567890001");

        romaneio.setTransportador(transportador);
        return romaneio;
    }
}