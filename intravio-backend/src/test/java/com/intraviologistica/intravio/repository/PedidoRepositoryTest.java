package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.*;
import com.intraviologistica.intravio.model.enums.AcompanhaStatus;
import com.intraviologistica.intravio.model.enums.Prioridade;
import com.intraviologistica.intravio.model.enums.StatusPedido;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class PedidoRepositoryTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FilialRepository filialRepository;

    @Autowired
    private RomaneioRepository romaneioRepository;

    @Autowired
    private TransportadorRepository transportadorRepository;

    @Test
    @DirtiesContext
    public void testSalvarPedido() {
        funcionarioRepository.save(FuncionarioTest.getFuncionario1());
        funcionarioRepository.save(FuncionarioTest.getFuncionario2());
        filialRepository.save(FilialTest.getFilial1());
        filialRepository.save(FilialTest.getFilial2());

        // instância item
        Item item = getItem();

        // cria um pedido com um item
        Pedido pedido = PedidoTest.getPedido();
        pedido.setItens(List.of(item));

        // salva o pedido
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        assertThat(pedidoSalvo.getId()).isNotNull();
        assertThat(pedidoSalvo.getItens()).hasSize(1);
        assertThat(pedidoSalvo.getItens().get(0).getPedido().getId()).isEqualTo(pedidoSalvo.getId());
        assertThat(pedidoSalvo.getItens().get(0).getId().getProduto().getId()).isNotNull();
        assertThat(pedidoSalvo.getItens().get(0).getPeso()).isEqualTo(1.0);
        assertThat(pedidoSalvo.getItens().get(0).getPreco()).isEqualTo(10.0);
        assertThat(pedidoSalvo.getItens().get(0).getPrecoTotal()).isEqualTo(10.0);
        assertThat(pedidoSalvo.getItens().get(0).getPesoTotal()).isEqualTo(1.0);
        assertThat(pedido.getItens().get(0).getDescricao()).isEqualTo(item.getDescricao());
        assertThat(pedido.getItens().get(0).getQuantidade()).isEqualTo(1);
        assertThat(pedido.getItens().get(0).getPedido().getId()).isEqualTo(pedidoSalvo.getId());
        assertThat(pedido.getItens().get(0).getProduto().getId()).isEqualTo("id1");

    }

    @Test
    void testBuscarPedidoPorId() {
        funcionarioRepository.save(FuncionarioTest.getFuncionario1());
        funcionarioRepository.save(FuncionarioTest.getFuncionario2());
        filialRepository.save(FilialTest.getFilial1());
        filialRepository.save(FilialTest.getFilial2());

        Pedido pedido = PedidoTest.getPedido();
        pedido.setItens(List.of(getItem()));
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        String idPedido = pedidoSalvo.getId();

        Optional<Pedido> pedidoEncontrado = pedidoRepository.findById(idPedido);

        assertThat(pedidoEncontrado)
                .isPresent()
                .get()
                .extracting(Pedido::getId)
                .isEqualTo(idPedido);
        assertThat(pedidoEncontrado.get().getStatusPedido()).isEqualTo(StatusPedido.PENDENTE);
        assertThat(pedidoEncontrado.get().getNumeroPedido()).isEqualTo(1);
        assertThat(pedidoEncontrado.get().getPrioridade()).isEqualTo(Prioridade.BAIXA);
        assertThat(pedidoEncontrado.get().getCodigoRastreio()).isEqualTo("c20000l");
        assertThat(pedidoEncontrado.get().getDataPedido()).isNotNull();
        assertThat(pedidoEncontrado.get().getDataAtualizacao()).isNotNull();
        assertThat(pedidoEncontrado.get().getItens()).isNotEmpty();
        assertThat(pedidoEncontrado.get().getHistoricoPedidos()).isNotEmpty();
        assertThat(pedidoEncontrado.get().getHistoricoPedidos().get(0).getId()).isNotNull();
        assertThat(pedidoEncontrado.get().getHistoricoPedidos().get(0).getId()).isEqualTo(1);
        assertThat(pedidoEncontrado.get().getHistoricoPedidos().get(0).getStatusAnterior()).isNull();
        assertThat(pedidoEncontrado.get().getHistoricoPedidos().get(0).getStatusAtual()).isEqualTo(StatusPedido.PENDENTE);
        assertThat(pedidoEncontrado.get().getHistoricoPedidos().get(0).getComentario()).isEqualTo("Pedido Teste");
        assertThat(pedidoEncontrado.get().getHistoricoPedidos().get(0).getDataAtualizacao()).isNotNull();


    }

    @Test
    void testBuscarPedidoPorNumeroPedido() {
        funcionarioRepository.save(FuncionarioTest.getFuncionario1());
        funcionarioRepository.save(FuncionarioTest.getFuncionario2());
        filialRepository.save(FilialTest.getFilial1());
        filialRepository.save(FilialTest.getFilial2());

        Pedido pedido = getPedido();
        pedido.setNumeroPedido(1);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        Integer numeroPedido = pedidoSalvo.getNumeroPedido();

        Optional<Pedido> pedidoEncontrado = pedidoRepository.findByNumeroPedido(numeroPedido);

        assertThat(pedidoEncontrado)
                .isPresent()
                .get()
                .extracting(Pedido::getNumeroPedido)
                .isEqualTo(numeroPedido);
    }

    @Test
    public void testBuscarTodosPedidos() {
        funcionarioRepository.save(FuncionarioTest.getFuncionario1());
        funcionarioRepository.save(FuncionarioTest.getFuncionario2());

        filialRepository.save(FilialTest.getFilial1());
        filialRepository.save(FilialTest.getFilial2());

        // Pedido 1
        Pedido pedido1 = getPedido();
        pedido1.setId("id1");
        pedido1.setNumeroPedido(1);

        // Pedido 2
        Pedido pedido2 = getPedido();
        pedido2.setNumeroPedido(2);
        pedido2.setId("id2");

        // Pedido 3
        Pedido pedido3 = getPedido();
        pedido3.setNumeroPedido(3);
        pedido3.setId("id3");

        pedido1 = pedidoRepository.save(pedido1);
        pedido2 = pedidoRepository.save(pedido2);
        pedido3 = pedidoRepository.save(pedido3);

        List<Pedido> pedidos = pedidoRepository.findAll();

        assertThat(pedidos.size()).isEqualTo(3);
        assertThat(pedidos.contains(pedido1)).isTrue();
        assertThat(pedidos.contains(pedido2)).isTrue();
        assertThat(pedidos.contains(pedido3)).isTrue();
    }

    @Test
    public void testAtualizaPedido() {
        Funcionario funcionario1 = funcionarioRepository.save(FuncionarioTest.getFuncionario1());
        Funcionario funcionario2 = funcionarioRepository.save(FuncionarioTest.getFuncionario2());

        Filial filialA = filialRepository.save(FilialTest.getFilial1());
        Filial filialB = filialRepository.save(FilialTest.getFilial2());

        Pedido pedido = getPedido();

        pedido = pedidoRepository.save(pedido);

        pedido.setOrigem(filialB);
        pedido.setDestino(filialA);
        pedido.setRemetente(funcionario2);
        pedido.setDestinatario(funcionario1);
        pedido.setAcompanhaStatus(AcompanhaStatus.SIM_REMETENTE);
        pedido.atualizarStatus(StatusPedido.ENTREGUE, "Objeto entregue");
        pedido.setPrioridade(Prioridade.URGENTE);

        Pedido pedidoAtualizado = pedidoRepository.save(pedido);

        assertThat(pedidoAtualizado).isNotNull();
        assertThat(pedidoAtualizado.getId()).isEqualTo(pedido.getId());
        assertThat(pedidoAtualizado.getRemetente()).isEqualTo(pedido.getRemetente());
        assertThat(pedidoAtualizado.getDestinatario()).isEqualTo(pedido.getDestinatario());
        assertThat(pedidoAtualizado.getOrigem()).isEqualTo(pedido.getOrigem());
        assertThat(pedidoAtualizado.getDestino()).isEqualTo(pedido.getDestino());
        assertThat(pedidoAtualizado.getStatus()).isEqualTo(StatusPedido.ENTREGUE);
        assertThat(pedidoAtualizado.getRomaneio()).isEqualTo(pedido.getRomaneio());
        assertThat(pedidoAtualizado.getDataPedido()).isEqualTo(pedido.getDataPedido());
        assertThat(pedidoAtualizado.getDataAtualizacao()).isEqualTo(pedido.getDataAtualizacao());
        assertThat(pedidoAtualizado.getAcompanhaStatus()).isEqualTo(AcompanhaStatus.SIM_REMETENTE);
        assertThat(pedidoAtualizado.getHistoricoPedidos()).isEqualTo(pedido.getHistoricoPedidos());
        assertThat(pedidoAtualizado.getImagens()).isEqualTo(pedido.getImagens());
        assertThat(pedidoAtualizado.getPesoTotal()).isEqualTo(pedido.getPesoTotal());
        assertThat(pedidoAtualizado.getValorTotal()).isEqualTo(pedido.getValorTotal());
        assertThat(pedidoAtualizado.getCodigoRastreio()).isEqualTo(pedido.getCodigoRastreio());
        assertThat(pedidoAtualizado.getPrioridade()).isEqualTo(pedido.getPrioridade());
    }

    @Test
    public void testExcluirPedidoPorId() {
        Funcionario funcionario1 = funcionarioRepository.save(FuncionarioTest.getFuncionario1());
        Funcionario funcionario2 = funcionarioRepository.save(FuncionarioTest.getFuncionario2());

        Filial filialA = filialRepository.save(FilialTest.getFilial1());
        Filial filialB = filialRepository.save(FilialTest.getFilial2());

        Pedido pedido = getPedido();
        pedido.setOrigem(filialA);
        pedido.setDestino(filialB);
        pedido.setRemetente(funcionario1);
        pedido.setDestinatario(funcionario2);
        pedido = pedidoRepository.save(getPedido());

        Optional<Pedido> pedidoSalvo = pedidoRepository.findById(pedido.getId());

        assertThat(pedidoSalvo).isPresent();

        pedidoRepository.deleteById(pedido.getId());

        Optional<Pedido> pedidoExcluido = pedidoRepository.findById(pedido.getId());

        assertThat(pedidoExcluido).isNotPresent();
    }

    @Test
    public void testFindByRomaneioId() {
        Funcionario funcionario1 = funcionarioRepository.save(FuncionarioTest.getFuncionario1());
        Funcionario funcionario2 = funcionarioRepository.save(FuncionarioTest.getFuncionario2());

        Filial filialA = filialRepository.save(FilialTest.getFilial1());
        Filial filialB = filialRepository.save(FilialTest.getFilial2());

        transportadorRepository.save(TransportadorTest.getTransportador());

        Romaneio romaneio1 = RomaneioTest.getRomaneio();
        romaneio1 = romaneioRepository.save(romaneio1);

        Romaneio romaneio2 = RomaneioTest.getRomaneio();
        romaneio2.setId("id2");
        romaneio2 = romaneioRepository.save(romaneio2);

        Romaneio romaneio3 = RomaneioTest.getRomaneio();
        romaneio3.setId("id3");
        romaneio3 = romaneioRepository.save(romaneio3);

        Pedido pedido1 = getPedido();
        pedido1.setRemetente(funcionario1);
        pedido1.setDestinatario(funcionario2);
        pedido1.setOrigem(filialA);
        pedido1.setDestino(filialB);
        pedido1.setRomaneio(romaneio1);

        Pedido pedido2 = getPedido();
        pedido2.setId("id2");
        pedido2.setNumeroPedido(2);
        pedido2.setRemetente(funcionario1);
        pedido2.setDestinatario(funcionario1);
        pedido2.setOrigem(filialB);
        pedido2.setDestino(filialA);
        pedido2.setRomaneio(romaneio1);

        Pedido pedido3 = getPedido();
        pedido3.setId("id3");
        pedido3.setNumeroPedido(3);
        pedido3.setRemetente(funcionario1);
        pedido3.setDestinatario(funcionario2);
        pedido3.setOrigem(filialA);
        pedido3.setDestino(filialB);
        pedido3.setRomaneio(romaneio3);

        Pedido pedido4 = getPedido();
        pedido4.setId("id4");
        pedido4.setNumeroPedido(4);
        pedido4.setRemetente(funcionario1);
        pedido4.setDestinatario(funcionario2);
        pedido4.setOrigem(filialA);
        pedido4.setDestino(filialB);
        pedido4.setRomaneio(romaneio2);

        pedidoRepository.save(pedido1);
        pedidoRepository.save(pedido2);
        pedidoRepository.save(pedido3);
        pedidoRepository.save(pedido4);

        // Buscando todos os pedidos com o romaneio de id 1
        List<Pedido> pedidosEncontrados = pedidoRepository.findByRomaneioId("id1");
        assertThat(pedidosEncontrados.size()).isEqualTo(2);

        // Buscando todos os pedidos com o romaneio de id 2
        pedidosEncontrados = pedidoRepository.findByRomaneioId("id2");
        assertThat(pedidosEncontrados.size()).isEqualTo(1);

        // Buscando todos os pedidos com o romaneio de id 3
        pedidosEncontrados = pedidoRepository.findByRomaneioId("id3");
        assertThat(pedidosEncontrados.size()).isEqualTo(1);
    }

    private Item getItem() {
        ItemPedidoPK pk = new ItemPedidoPK();
        pk.setPedido(PedidoTest.getPedido());
        pk.getPedido().setId("41bc-ab36");
        pk.setProduto(ProdutoTest.getProduto1());
        pk.getProduto().setId("id1");

        Item item = new Item();
        item.setId(pk);
        item.setDescricao("Item teste");
        item.setQuantidade(1);
        item.setPreco(10.0);
        item.setPeso(1.0);
        return item;
    }

    private Pedido getPedido() {
        Pedido pedido = new Pedido();
        pedido.setId("41bc-ab36");
        pedido.setNumeroPedido(1);
        pedido.setOrigem(FilialTest.getFilial1());
        pedido.setDestino(FilialTest.getFilial2());
        pedido.setRemetente(FuncionarioTest.getFuncionario1());
        pedido.setDestinatario(FuncionarioTest.getFuncionario2());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setDataAtualizacao(LocalDateTime.now());
        pedido.setPrioridade(Prioridade.BAIXA);
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setAcompanhaStatus(AcompanhaStatus.NAO);
        pedido.setCodigoRastreio("c20000l");
        return pedido;
    }
}