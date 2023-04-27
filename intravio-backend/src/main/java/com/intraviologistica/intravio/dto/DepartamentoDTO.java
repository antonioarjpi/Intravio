package com.intraviologistica.intravio.dto;

import jakarta.validation.constraints.NotBlank;

public class DepartamentoDTO {

    private Long id;
    @NotBlank(message = "Preenchimento do nome do departamento é obrigatório")
    private String nome;

    public DepartamentoDTO() {
    }

    public DepartamentoDTO(Long id, String nome) {
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
