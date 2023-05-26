package com.intraviologistica.intravio.model;

import java.time.LocalDateTime;

public class ProdutoTest {

    public static Produto getProduto1() {
        Produto produto = new Produto();
        produto.setId("a412bzzx");
        produto.setCodigo(123);
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

    public static Produto getProduto2() {
        Produto smartphone = new Produto();
        smartphone.setId("ab24acd");
        smartphone.setCodigo(124);
        smartphone.setNome("Smartphone");
        smartphone.setDescricao("Smartphone Samsung Galaxy S20");
        smartphone.setPreco(4000.00);
        smartphone.setPeso(0.3);
        smartphone.setFabricante("Samsung");
        smartphone.setModelo("Galaxy S20");
        smartphone.setDataCriacao(LocalDateTime.now());
        smartphone.setDataAtualizacao(LocalDateTime.now());
        return smartphone;
    }

}