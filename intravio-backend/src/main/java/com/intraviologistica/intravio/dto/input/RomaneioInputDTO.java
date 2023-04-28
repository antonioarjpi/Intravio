package com.intraviologistica.intravio.dto.input;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class RomaneioInputDTO {

    private Long id;

    @NotNull(message = "A lista de pedidos não pode ser nula")
    @NotEmpty(message = "Não é possível criar romaneio sem pedido")
    private List<Long> pedidos;
    private Long transportadorCodigo;
    private Double taxaFrete;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String observacao;
    private Integer cnpj;
    private boolean isProcessa;

    public RomaneioInputDTO() {
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

    public boolean isProcessa() {
        return isProcessa;
    }

    public void setProcessa(boolean processa) {
        isProcessa = processa;
    }

    public Integer getCnpj() {
        return cnpj;
    }

    public void setCnpj(Integer cnpj) {
        this.cnpj = cnpj;
    }
}
