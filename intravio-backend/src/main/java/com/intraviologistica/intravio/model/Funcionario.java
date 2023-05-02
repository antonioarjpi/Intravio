package com.intraviologistica.intravio.model;

import jakarta.persistence.*;

@Entity
public class Funcionario {

    @Id
    private String id;

    private String nome;
    @Column(unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @ManyToOne
    @JoinColumn(name = "filial_id")
    private Filial filial;

    public Funcionario() {
    }

    public Funcionario(String id, String nome, String email, Departamento departamento, Filial filial) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.departamento = departamento;
        this.filial = filial;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }
}
