package com.intraviologistica.intravio.model;

import static org.junit.jupiter.api.Assertions.*;

public class ItemPedidoPKTest {

    public static ItemPedidoPK getItemPK(){
        return new ItemPedidoPK(PedidoTest.getPedido(), ProdutoTest.getProduto1());
    }
}