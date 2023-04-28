package com.intraviologistica.intravio.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.intraviologistica.intravio.model.Romaneio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RomaneioDTO {

    private String id;
    private Integer numeroRomaneio;
    private String transportadora;
    private String placa;
    private String veiculo;
    private String motorista;
    private Integer statusRomaneio;
    private Double taxaFrete;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataCriacao;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataAtualizacao;
    private String observacao;
    private Double pesoCarga;
    private Double valorCarga;
    private List<PedidoDTO> pedidos = new ArrayList<>();


    public RomaneioDTO() {
    }

    public RomaneioDTO(Romaneio romaneio) {
        this.id = romaneio.getId();
        this.pedidos = romaneio.getPedidos().stream().map(x -> new PedidoDTO(x)).collect(Collectors.toList());
        this.transportadora = romaneio.getTransportador().getNome();
        this.placa = romaneio.getTransportador().getPlaca();
        this.veiculo = romaneio.getTransportador().getVeiculo();
        this.motorista = romaneio.getTransportador().getMotorista();
        this.statusRomaneio = romaneio.getStatus().ordinal();
        this.taxaFrete = romaneio.getTaxaFrete();
        this.dataCriacao = romaneio.getDataCriacao();
        this.dataAtualizacao = romaneio.getDataAtualizacao();
        this.observacao = romaneio.getObservacao();
        this.valorCarga = romaneio.getValorTotal();
        this.pesoCarga = romaneio.getPesoTotal();
        this.numeroRomaneio = romaneio.getNumeroRomaneio();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PedidoDTO> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<PedidoDTO> pedidos) {
        this.pedidos = pedidos;
    }

    public String getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(String transportadora) {
        this.transportadora = transportadora;
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

    public String getMotorista() {
        return motorista;
    }

    public void setMotorista(String motorista) {
        this.motorista = motorista;
    }

    public Integer getStatusRomaneio() {
        return statusRomaneio;
    }

    public void setStatusRomaneio(Integer statusRomaneio) {
        this.statusRomaneio = statusRomaneio;
    }

    public Double getTaxaFrete() {
        return taxaFrete;
    }

    public void setTaxaFrete(Double taxaFrete) {
        this.taxaFrete = taxaFrete;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Double getPesoCarga() {
        return pesoCarga;
    }

    public void setPesoCarga(Double pesoCarga) {
        this.pesoCarga = pesoCarga;
    }

    public Double getValorCarga() {
        return valorCarga;
    }

    public void setValorCarga(Double valorCarga) {
        this.valorCarga = valorCarga;
    }

    public Integer getNumeroRomaneio() {
        return numeroRomaneio;
    }

    public void setNumeroRomaneio(Integer numeroRomaneio) {
        this.numeroRomaneio = numeroRomaneio;
    }
}
