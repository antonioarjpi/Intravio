package com.intraviologistica.intravio.dto;

import jakarta.validation.constraints.NotBlank;

public class FuncionarioDTO {

    private String id;
    @NotBlank(message = "Preenchimento do nome é obrigatório")
    private String nome;
    @NotBlank(message = "Preenchimento do e-mail é obrigatório")
    private String email;
    private String departamento;
    private Long filial;

    public FuncionarioDTO() {
    }

    public FuncionarioDTO(String id, String nome, String email, String departamento, Long filial) {
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

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Long getFilial() {
        return filial;
    }

    public void setFilial(Long filial) {
        this.filial = filial;
    }
}
