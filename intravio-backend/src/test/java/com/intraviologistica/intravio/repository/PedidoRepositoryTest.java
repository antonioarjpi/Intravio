package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.*;
import com.intraviologistica.intravio.model.enums.AcompanhaStatus;
import com.intraviologistica.intravio.model.enums.Prioridade;
import com.intraviologistica.intravio.model.enums.StatusPedido;
import com.intraviologistica.intravio.model.enums.StatusRomaneio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
        Funcionario funcionario1 = funcionarioRepository.save(getFuncionarioA());
        Funcionario funcionario2 = funcionarioRepository.save(getFuncionarioB());

        Filial filialA = filialRepository.save(getFilialA());
        Filial filialB = filialRepository.save(getFilialB());

        // cria um pedido com um item
        Pedido pedido = getPedido();
        pedido.setId("id1");
        pedido.setNumeroPedido(1);
        pedido.setOrigem(filialA);
        pedido.setDestino(filialB);
        pedido.setRemetente(funcionario1);
        pedido.setDestinatario(funcionario2);
        pedido.setItens(Arrays.asList(getItem()));

        // salva o pedido
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        assertThat(pedidoSalvo.getId()).isNotNull();
        assertThat(pedidoSalvo.getItens()).hasSize(1);
        assertThat(pedidoSalvo.getItens().get(0).getPedido().getId()).isEqualTo(pedidoSalvo.getId());
        assertThat(pedidoSalvo.getItens().get(0).getId().getProduto().getId()).isNotNull();
    }

    @Test
    void testBuscarPedidoPorId() {
        Funcionario funcionario1 = funcionarioRepository.save(getFuncionarioA());
        Funcionario funcionario2 = funcionarioRepository.save(getFuncionarioB());

        Filial filialA = filialRepository.save(getFilialA());
        Filial filialB = filialRepository.save(getFilialB());

        Pedido pedidoSalvo = getPedido();
        pedidoSalvo.setNumeroPedido(1);
        pedidoSalvo.setOrigem(filialA);
        pedidoSalvo.setDestino(filialB);
        pedidoSalvo.setRemetente(funcionario1);
        pedidoSalvo.setDestinatario(funcionario2);

        pedidoSalvo = pedidoRepository.save(pedidoSalvo);

        String idPedido = pedidoSalvo.getId();

        Optional<Pedido> pedidoEncontrado = pedidoRepository.findById(idPedido);

        assertThat(pedidoEncontrado)
                .isPresent()
                .get()
                .extracting(Pedido::getId)
                .isEqualTo(idPedido);
    }

    @Test
    void testBuscarPedidoPorNumeroPedido() {
        Funcionario funcionario1 = funcionarioRepository.save(getFuncionarioA());
        Funcionario funcionario2 = funcionarioRepository.save(getFuncionarioB());

        Filial filialA = filialRepository.save(getFilialA());
        Filial filialB = filialRepository.save(getFilialB());

        Pedido pedidoSalvo = getPedido();
        pedidoSalvo.setNumeroPedido(1);
        pedidoSalvo.setOrigem(filialA);
        pedidoSalvo.setDestino(filialB);
        pedidoSalvo.setRemetente(funcionario1);
        pedidoSalvo.setDestinatario(funcionario2);

        pedidoSalvo = pedidoRepository.save(pedidoSalvo);

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
        Funcionario funcionario1 = funcionarioRepository.save(getFuncionarioA());
        Funcionario funcionario2 = funcionarioRepository.save(getFuncionarioB());

        Filial filialA = filialRepository.save(getFilialA());
        Filial filialB = filialRepository.save(getFilialB());

        Pedido pedido1 = getPedido();
        pedido1.setId("id1");
        pedido1.setNumeroPedido(1);
        pedido1.setRemetente(funcionario1);
        pedido1.setDestinatario(funcionario2);
        pedido1.setOrigem(filialA);
        pedido1.setDestino(filialB);

        Pedido pedido2 = getPedido();
        pedido2.setNumeroPedido(2);
        pedido2.setId("id2");
        pedido2.setRemetente(funcionario1);
        pedido2.setDestinatario(funcionario2);
        pedido2.setOrigem(filialA);
        pedido2.setDestino(filialB);

        Pedido pedido3 = getPedido();
        pedido3.setNumeroPedido(3);
        pedido3.setId("id3");
        pedido3.setRemetente(funcionario1);
        pedido3.setDestinatario(funcionario2);
        pedido3.setOrigem(filialA);
        pedido3.setDestino(filialB);

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
        Funcionario funcionario1 = funcionarioRepository.save(getFuncionarioA());
        Funcionario funcionario2 = funcionarioRepository.save(getFuncionarioB());

        Filial filialA = filialRepository.save(getFilialA());
        Filial filialB = filialRepository.save(getFilialB());

        List<String> imagens = new ArrayList<>();
        imagens.add("imagem.png");

        Pedido pedido = getPedido();
        pedido.setOrigem(filialA);
        pedido.setDestino(filialB);
        pedido.setRemetente(funcionario1);
        pedido.setDestinatario(funcionario2);
        pedido.setCodigoRastreio("Y0000001");
        pedido.setImagens(imagens);

        pedido = pedidoRepository.save(pedido);

        pedido.setOrigem(filialB);
        pedido.setDestino(filialA);
        pedido.setRemetente(funcionario2);
        pedido.setDestinatario(funcionario1);
        pedido.setAcompanhaStatus(AcompanhaStatus.SIM_REMETENTE);
        pedido.atualizarStatus(StatusPedido.ENTREGUE, "Objeto entregue");
        imagens.add("imagens.jpg");
        pedido.setImagens(imagens);
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
        Funcionario funcionario1 = funcionarioRepository.save(getFuncionarioA());
        Funcionario funcionario2 = funcionarioRepository.save(getFuncionarioB());

        Filial filialA = filialRepository.save(getFilialA());
        Filial filialB = filialRepository.save(getFilialB());

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
        Funcionario funcionario1 = funcionarioRepository.save(getFuncionarioA());
        Funcionario funcionario2 = funcionarioRepository.save(getFuncionarioB());

        Filial filialA = filialRepository.save(getFilialA());
        Filial filialB = filialRepository.save(getFilialB());

        Romaneio romaneio1 = getRomaneio();
        romaneio1.setId("id1");
        romaneio1 = romaneioRepository.save(romaneio1);
        Romaneio romaneio2 = getRomaneio();
        romaneio2.setId("id2");
        romaneio2 = romaneioRepository.save(romaneio2);
        Romaneio romaneio3 = getRomaneio();
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

    private Romaneio getRomaneio() {
        Romaneio romaneio = new Romaneio();
        romaneio.setId("id1");
        romaneio.setStatus(StatusRomaneio.ABERTO);
        romaneio.setObservacao("Observação");
        romaneio.setTaxaFrete(3.00);
        romaneio.setTransportador(getTransportador());
        romaneio.setDataCriacao(LocalDateTime.now());
        romaneio.setDataAtualizacao(LocalDateTime.now());
        return romaneio;
    }

    private Transportador getTransportador() {
        Transportador transportador = new Transportador();
        transportador.setId("tid1");
        transportador.setNome("VIP Transportadora");
        transportador.setMotorista("Carlos");
        transportador.setPlaca("PPP-1111");
        transportador.setVeiculo("Mercedes-Benz");
        transportador.setObservacao("Observacao");
        return transportadorRepository.save(transportador);
    }

    private Item getItem() {
        ItemPedidoPK id = new ItemPedidoPK();
        id.setPedido(getPedido());
        id.getPedido().setId("id1");
        id.setProduto(getProduto());
        id.getProduto().setId("id1");
        Item item = new Item();
        item.setId(id);
        item.setDescricao("Item teste");
        item.setQuantidade(1);
        item.setPreco(10.0);
        item.setPeso(1.0);
        return item;
    }

    private Produto getProduto() {
        Produto produto = new Produto();
        produto.setNome("Notebook");
        produto.setDescricao("Notebook Dell Inspiron 15");
        produto.setPreco(3500.00);
        produto.setPeso(2.0);
        produto.setFabricante("Dell");
        produto.setModelo("Inspiron 15");
        produto.setDataCriacao(LocalDateTime.now());
        produto.setDataAtualizacao(LocalDateTime.now());
        return produto;
    }

    private Filial getFilialA() {
        Filial filial = new Filial();
        filial.setId(1l);
        filial.setNome("Filial A");
        return filial;
    }

    private Filial getFilialB() {
        Filial filialB = new Filial();
        filialB.setId(2l);
        filialB.setNome("Filial B");
        return filialB;
    }

    private Funcionario getFuncionarioA() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId("fidA");
        funcionario.setNome("João");
        funcionario.setEmail("joao@teste.com");
        return funcionario;
    }

    private Funcionario getFuncionarioB() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId("fidB");
        funcionario.setNome("Maria");
        funcionario.setEmail("maria@teste.com");
        return funcionario;
    }

    private Pedido getPedido() {
        Pedido pedido = new Pedido();
        pedido.setId("id");
        pedido.setNumeroPedido(1);
        pedido.setOrigem(getFilialA());
        pedido.setDestino(getFilialB());
        pedido.setRemetente(getFuncionarioA());
        pedido.setDestinatario(getFuncionarioB());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setDataAtualizacao(LocalDateTime.now());
        pedido.setPrioridade(Prioridade.BAIXA);
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setAcompanhaStatus(AcompanhaStatus.NAO);
        pedido.setCodigoRastreio("c20000l");
        return pedido;
    }
}