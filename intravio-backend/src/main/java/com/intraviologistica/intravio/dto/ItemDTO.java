package com.intraviologistica.intravio.dto;

import com.intraviologistica.intravio.model.Item;

public class ItemDTO {

    private String produtoNome;
    private Double produtoPreco;
    private String descricao;
    private Integer quantidade;
    private Double subtotalPeso;
    private Double subtotalPreco;

    public ItemDTO() {
    }

    public ItemDTO(Item item) {
       this.produtoNome = item.getProduto().getNome();
       this.produtoPreco = item.getProduto().getPreco();
       this.descricao = item.getDescricao();
       this.quantidade = item.getQuantidade();
       this.subtotalPeso = item.getPesoTotal();
       this.subtotalPreco = item.getPrecoTotal();
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public Double getProdutoPreco() {
        return produtoPreco;
    }

    public void setProdutoPreco(Double produtoPreco) {
        this.produtoPreco = produtoPreco;
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

    public Double getPeso() {
        return subtotalPeso;
    }

    public void setPeso(Double peso) {
        this.subtotalPeso = peso;
    }

    public Double getSubtotalPreco() {
        return subtotalPreco;
    }

    public void setSubtotalPreco(Double subtotalPreco) {
        this.subtotalPreco = subtotalPreco;
    }
}
