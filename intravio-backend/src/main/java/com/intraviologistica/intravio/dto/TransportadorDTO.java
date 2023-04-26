package com.intraviologistica.intravio.dto;

public class TransportadorDTO {

    private Long id;
    private String nome;
    private String motorista;
    private String placa;
    private String veiculo;
    private String observacao;

    public TransportadorDTO() {
    }

    public TransportadorDTO(Long id, String nome, String motorista, String placa, String veiculo, String observacao) {
        this.id = id;
        this.nome = nome;
        this.motorista = motorista;
        this.placa = placa;
        this.veiculo = veiculo;
        this.observacao = observacao;
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
}

