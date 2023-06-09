package com.intraviologistica.intravio.dto;

import com.intraviologistica.intravio.model.Funcionario;

public class FuncionarioDTO {

    private String id;
    private String nome;
    private String email;
    private String departamento;
    private String filial;

    public FuncionarioDTO() {
    }

    public FuncionarioDTO(Funcionario funcionario) {
        this.id = funcionario.getId();
        this.nome = funcionario.getNome();
        this.email = funcionario.getEmail();
        this.departamento = funcionario.getDepartamento() != null ? funcionario.getDepartamento().getNome() : null;
        this.filial = funcionario.getFilial() != null ? funcionario.getFilial().getNome() : null;
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

    public String getFilial() {
        return filial;
    }

    public void setFilial(String filial) {
        this.filial = filial;
    }
}
