package com.intraviologistica.intravio.dto;

public class ItemDTO {

    private Long id;
    private String descricao;
    private Integer quantidade;

    public ItemDTO() {
    }

    public ItemDTO(Long id, String descricao, Integer quantidade, Double preco, Double peso) {
        this.id = id;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
