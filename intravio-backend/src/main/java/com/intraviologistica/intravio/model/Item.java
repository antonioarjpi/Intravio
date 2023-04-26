package com.intraviologistica.intravio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "itens")
public class Item {

    @JsonIgnore
    @EmbeddedId
    private ItemPedidoPK id = new ItemPedidoPK();

    private String descricao;
    private Integer quantidade;
    @JsonIgnore
    private Double preco;
    @JsonIgnore
    private Double peso;

    public Item() {
    }

    public Item(Pedido pedido, Produto produto, String descricao, Integer quantidade, Double peso, Double preco) {
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.preco = preco;
        this.peso = peso;
        id.setProduto(produto);
        id.setPedido(pedido);
    }

    public Double getSubtotal() {
        return this.preco * quantidade;
    }

    public Double getPesoTotal() {
        return this.peso * quantidade;
    }

    public ItemPedidoPK getId() {
        return id;
    }

    public void setId(ItemPedidoPK id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    @JsonIgnore
    public Pedido getPedido() {
        return id.getPedido();
    }

    public void setPedido(Pedido pedido) {
        id.setPedido(pedido);
    }

    public Produto getProduto() {
        return id.getProduto();
    }

    public void setProduto(Produto produto) {
        id.setProduto(produto);
    }
}
