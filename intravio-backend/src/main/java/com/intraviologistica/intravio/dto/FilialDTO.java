package com.intraviologistica.intravio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FilialDTO {

    @NotNull(message = "Preenchimento do código da filial é obrigatório")
    private Long id;
    @NotBlank(message = "Preenchimento do nome da filial é obrigatório")
    private String nome;
    private String rua;
    private String cep;
    private String bairro;
    private Integer numero;
    private String complemento;
    @NotBlank(message = "Preenchimento do Estado é obrigatório")
    private String estado;
    @NotBlank(message = "Preenchimento da Cidade é obrigatório")
    private String cidade;

    public FilialDTO() {
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

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }
}
