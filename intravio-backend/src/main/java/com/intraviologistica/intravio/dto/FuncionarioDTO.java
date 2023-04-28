package com.intraviologistica.intravio.dto;

import jakarta.validation.constraints.NotBlank;

public class FuncionarioDTO {

    private String id;
    @NotBlank(message = "Preenchimento do nome é obrigatório")
    private String nome;
    @NotBlank(message = "Preenchimento do e-mail é obrigatório")
    private String email;
    private String departamentoNome;

    public FuncionarioDTO() {
    }

    public FuncionarioDTO(String id, String nome, String email, String departamentoNome) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.departamentoNome = departamentoNome;
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

    public String getDepartamentoNome() {
        return departamentoNome;
    }

    public void setDepartamentoNome(String departamentoNome) {
        this.departamentoNome = departamentoNome;
    }
}
