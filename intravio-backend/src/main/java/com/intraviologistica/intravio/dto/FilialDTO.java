package com.intraviologistica.intravio.dto;

public class FilialDTO {

    private Long id;
    private String nome;

    public FilialDTO() {
    }

    public FilialDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
