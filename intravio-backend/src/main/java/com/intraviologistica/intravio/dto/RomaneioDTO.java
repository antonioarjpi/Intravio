package com.intraviologistica.intravio.dto;

import com.intraviologistica.intravio.model.enums.StatusRomaneio;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class RomaneioDTO {

    private Long id;

    @NotNull(message = "A lista de pedidos não pode ser nula")
    @NotEmpty(message = "A lista de pedidos não pode estar vazia")
    private List<Long> pedidos;
    private Long transportadorCodigo;
    private StatusRomaneio Status;
    private Double taxaFrete;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String observacao;

    public RomaneioDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Long> pedidos) {
        this.pedidos = pedidos;
    }

    public Long getTransportadorCodigo() {
        return transportadorCodigo;
    }

    public void setTransportadorCodigo(Long transportadorCodigo) {
        this.transportadorCodigo = transportadorCodigo;
    }

    public StatusRomaneio getStatus() {
        return Status;
    }

    public void setStatus(StatusRomaneio status) {
        Status = status;
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
}
