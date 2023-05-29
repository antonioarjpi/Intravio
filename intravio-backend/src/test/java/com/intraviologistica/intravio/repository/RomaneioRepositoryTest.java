package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.*;
import com.intraviologistica.intravio.model.enums.StatusRomaneio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
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

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FilialRepository filialRepository;

    @Test
    public void testSalvarRomaneio() {
        funcionarioRepository.save(FuncionarioTest.getFuncionario1());
        funcionarioRepository.save(FuncionarioTest.getFuncionario2());
        filialRepository.save(FilialTest.getFilial1());
        filialRepository.save(FilialTest.getFilial2());
        Pedido pedido = pedidoRepository.save(PedidoTest.getPedido());
        Transportador transportador = transportadorRepository.save(TransportadorTest.getTransportador());

        Romaneio romaneio = RomaneioTest.getRomaneio();
        romaneio.setPedidos(List.of(pedido));

        Romaneio romaneioSalvo = romaneioRepository.save(romaneio);

        assertThat(romaneioSalvo.getId()).isNotNull();
        assertThat(romaneioSalvo.getStatus()).isEqualTo(StatusRomaneio.ABERTO);
        assertThat(romaneioSalvo.getTaxaFrete()).isEqualTo(3.0);
        assertThat(romaneioSalvo.getNumeroRomaneio()).isEqualTo(1);
        assertThat(romaneioSalvo.getPedidos()).isNotEmpty();
        assertThat(romaneioSalvo.quantidadePedidos()).isEqualTo(1);
        assertThat(romaneioSalvo.getTransportador()).isEqualTo(transportador);

    }

    @Test
    public void testBuscarRomaneioPorId() {
        funcionarioRepository.save(FuncionarioTest.getFuncionario1());
        funcionarioRepository.save(FuncionarioTest.getFuncionario2());
        filialRepository.save(FilialTest.getFilial1());
        filialRepository.save(FilialTest.getFilial2());
        Pedido pedido = pedidoRepository.save(PedidoTest.getPedido());
        Transportador transportador = transportadorRepository.save(TransportadorTest.getTransportador());

        Romaneio romaneio = RomaneioTest.getRomaneio();
        romaneio.setPedidos(List.of(pedido));
        Romaneio romaneioSalvo = romaneioRepository.save(romaneio);

        Optional<Romaneio> romaneioEncontrado = romaneioRepository.findById(romaneioSalvo.getId());

        assertThat(romaneioEncontrado).isPresent();
        assertThat(romaneioEncontrado.get().getId()).isEqualTo(romaneioSalvo.getId());
        assertThat(romaneioEncontrado.get().getTaxaFrete()).isEqualTo(3.0);
        assertThat(romaneioEncontrado.get().getStatus()).isEqualTo(StatusRomaneio.ABERTO);
        assertThat(romaneioEncontrado.get().getNumeroRomaneio()).isEqualTo(1);
        assertThat(romaneioEncontrado.get().getPedidos()).isNotEmpty();
        assertThat(romaneioEncontrado.get().quantidadePedidos()).isEqualTo(1);
        assertThat(romaneioEncontrado.get().getTransportador()).isEqualTo(transportador);
        assertThat(romaneioEncontrado.get().getTransportador()).isNotNull();
    }

    @Test
    public void testAtualizarRomaneio() {
        transportadorRepository.save(TransportadorTest.getTransportador());

        Romaneio romaneio = romaneioRepository.save(RomaneioTest.getRomaneio());

        romaneio = new Romaneio(romaneio.getId(), null, null, StatusRomaneio.EM_TRANSITO, 15.00, romaneio.getDataCriacao(), LocalDateTime.now(), "Nova Observacao");
        romaneio.setNumeroRomaneio(1);
        romaneio = romaneioRepository.save(romaneio);

        Optional<Romaneio> romaneioAtualizado = romaneioRepository.findById(romaneio.getId());

        assertThat(romaneioAtualizado).isPresent();
        assertThat(romaneioAtualizado.get().getId()).isEqualTo(romaneio.getId());
        assertThat(romaneioAtualizado.get().getStatus()).isEqualTo(StatusRomaneio.EM_TRANSITO);
        assertThat(romaneioAtualizado.get().getDataAtualizacao()).isEqualTo(romaneio.getDataAtualizacao());
        assertThat(romaneioAtualizado.get().getTaxaFrete()).isEqualTo(romaneio.getTaxaFrete());
        assertThat(romaneioAtualizado.get().getObservacao()).isEqualTo(romaneio.getObservacao());
        assertThat(romaneioAtualizado.get().getNumeroRomaneio()).isEqualTo(1);
        assertThat(romaneioAtualizado.get().getPedidos()).isEqualTo(romaneio.getPedidos());
    }

    @Test
    public void testBuscarTodosRomaneios() {
        transportadorRepository.save(TransportadorTest.getTransportador());

        romaneioRepository.save(RomaneioTest.getRomaneio());

        Romaneio romaneio2 = new Romaneio();
        romaneio2.setId("id2");
        romaneio2.setTransportador(TransportadorTest.getTransportador());
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
        Transportador transportador = transportadorRepository.save(TransportadorTest.getTransportador());
        Romaneio romaneio = RomaneioTest.getRomaneio();
        romaneio.setTransportador(transportador);
        romaneio = romaneioRepository.save(romaneio);

        String id = romaneio.getId();

        romaneioRepository.deleteById(id);

        Optional<Romaneio> romaneioExcluido = romaneioRepository.findById(id);
        assertThat(romaneioExcluido.isPresent()).isFalse();
    }
}