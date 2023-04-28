package com.intraviologistica.intravio.dto;

import jakarta.validation.constraints.NotBlank;

public class TransportadorDTO {

    private String id;
    @NotBlank(message = "Preenchimento do nome do Transportador é obrigatório")
    private String nome;
    private String motorista;
    private String placa;
    private String veiculo;
    private String observacao;
    private String cnpj;

    public TransportadorDTO() {
    }

    public TransportadorDTO(String id, String nome, String motorista, String placa, String veiculo, String observacao, String cnpj) {
        this.id = id;
        this.nome = nome;
        this.motorista = motorista;
        this.placa = placa;
        this.veiculo = veiculo;
        this.observacao = observacao;
        this.cnpj = cnpj;
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

    public String getMotorista() {
        return motorista;
    }

    public void setMotorista(String motorista) {
        this.motorista = motorista;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}

