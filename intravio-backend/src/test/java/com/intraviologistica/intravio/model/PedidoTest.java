package com.intraviologistica.intravio.model;

import com.intraviologistica.intravio.model.enums.AcompanhaStatus;
import com.intraviologistica.intravio.model.enums.Prioridade;
import com.intraviologistica.intravio.model.enums.StatusPedido;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class PedidoTest {

    public static Pedido getPedido() {
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
        pedido.setImagens(List.of("imagem.png"));
        pedido.setAcompanhaStatus(AcompanhaStatus.NAO);
        pedido.setCodigoRastreio("c20000l");

        pedido.atualizarStatus(StatusPedido.PENDENTE, "Pedido Teste");

        return pedido;
    }

    @Test
    void testGetValorTotal() {
        Pedido pedido = new Pedido();
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        Item item3 = mock(Item.class);
        pedido.setItens(Arrays.asList(item1, item2, item3));
        Mockito.when(item1.getPrecoTotal()).thenReturn(10.0);
        Mockito.when(item2.getPrecoTotal()).thenReturn(20.0);
        Mockito.when(item3.getPrecoTotal()).thenReturn(30.0);

        assertThat(pedido.getValorTotal()).isEqualTo(60.0);
    }

    @Test
    void testGetPesoTotal() {
        Pedido pedido = new Pedido();
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        Item item3 = mock(Item.class);
        pedido.setItens(Arrays.asList(item1, item2, item3));
        Mockito.when(item1.getPesoTotal()).thenReturn(1.0);
        Mockito.when(item2.getPesoTotal()).thenReturn(2.0);
        Mockito.when(item3.getPesoTotal()).thenReturn(3.0);

        assertThat(pedido.getPesoTotal()).isEqualTo(6.0);
    }

    @Test
    void testAtualizarStatus() {
        Pedido pedido = new Pedido();
        StatusPedido statusInicial = StatusPedido.PENDENTE;
        pedido.setStatusPedido(statusInicial);

        StatusPedido novoStatus = StatusPedido.EM_TRANSITO;
        String comentario = "Comentário de teste";
        pedido.atualizarStatus(novoStatus, comentario);
        assertThat(pedido.getStatusPedido()).isEqualTo(novoStatus);

        List<HistoricoPedido> historicoPedidos = pedido.getHistoricoPedidos();
        assertThat(historicoPedidos).hasSize(1);
        HistoricoPedido historico = historicoPedidos.get(0);
        assertThat(historico.getPedido()).isEqualTo(pedido);
        assertThat(historico.getDataAtualizacao()).isNotNull();
        assertThat(historico.getStatusAnterior()).isEqualTo(statusInicial);
        assertThat(historico.getStatusAtual()).isEqualTo(novoStatus);
        assertThat(historico.getComentario()).isEqualTo(comentario);
    }

}