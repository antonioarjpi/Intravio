package com.intraviologistica.intravio.dto;

public class FuncionarioDTO {

    private Long id;
    private String nome;
    private String email;
    private String departamentoNome;

    public FuncionarioDTO() {
    }

    public FuncionarioDTO(Long id, String nome, String email, String departamentoNome) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.departamentoNome = departamentoNome;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartamentoNome() {
        return departamentoNome;
    }

    public void setDepartamentoNome(String departamentoNome) {
        this.departamentoNome = departamentoNome;
    }
}
