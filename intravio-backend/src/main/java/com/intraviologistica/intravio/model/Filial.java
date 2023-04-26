package com.intraviologistica.intravio.model;

import jakarta.persistence.*;

@Entity
public class Filial {

    @Id
    private Long id;

    @Column(unique = true)
    private String nome;

    public Filial() {
    }

    public Filial(Long id, String nome) {
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