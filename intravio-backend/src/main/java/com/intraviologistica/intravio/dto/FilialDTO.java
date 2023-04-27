package com.intraviologistica.intravio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FilialDTO {

    @NotNull(message = "Preenchimento do código da filial é obrigatório")
    private Long id;
    @NotBlank(message = "Preenchimento do nome da filial é obrigatório")
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
