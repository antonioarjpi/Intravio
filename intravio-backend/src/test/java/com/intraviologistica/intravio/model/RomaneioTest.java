package com.intraviologistica.intravio.model;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class RomaneioTest {


    @Test
    public void testGetValorTotal() {
        Romaneio romaneio = new Romaneio();
        Pedido pedido1 = mock(Pedido.class);
        Pedido pedido2 = mock(Pedido.class);
        Pedido pedido3 = mock(Pedido.class);
        romaneio.setPedidos(Arrays.asList(pedido1, pedido2, pedido3));

        Mockito.when(pedido1.getValorTotal()).thenReturn(10.0);
        Mockito.when(pedido2.getValorTotal()).thenReturn(20.0);
        Mockito.when(pedido3.getValorTotal()).thenReturn(30.0);

        assertThat(romaneio.getValorTotal()).isEqualTo(60.0);
    }

    @Test
    public void testGetPesoTotal() {
        Romaneio romaneio = new Romaneio();
        Pedido pedido1 = mock(Pedido.class);
        Pedido pedido2 = mock(Pedido.class);
        Pedido pedido3 = mock(Pedido.class);
        romaneio.setPedidos(Arrays.asList(pedido1, pedido2, pedido3));

        Mockito.when(pedido1.getPesoTotal()).thenReturn(10.0);
        Mockito.when(pedido2.getPesoTotal()).thenReturn(20.0);
        Mockito.when(pedido3.getPesoTotal()).thenReturn(30.0);

        assertThat(romaneio.getPesoTotal()).isEqualTo(60.0);
    }
}

