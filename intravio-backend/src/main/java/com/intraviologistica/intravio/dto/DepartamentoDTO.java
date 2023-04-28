package com.intraviologistica.intravio.dto;

import jakarta.validation.constraints.NotBlank;

public class DepartamentoDTO {

    private String id;
    @NotBlank(message = "Preenchimento do nome do departamento é obrigatório")
    private String nome;

    public DepartamentoDTO() {
    }

    public DepartamentoDTO(String id, String nome) {
        this.id = id;
        this.nome = nome;
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
}
