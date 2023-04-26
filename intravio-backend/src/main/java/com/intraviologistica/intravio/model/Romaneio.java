
package com.intraviologistica.intravio.model;

import com.intraviologistica.intravio.model.enums.StatusRomaneio;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Romaneio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "romaneio", cascade = CascadeType.PERSIST)
    private List<Pedido> pedidos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "trasportador_id")
    private Transportador transportador;

    @Enumerated(EnumType.STRING)
    private StatusRomaneio Status;

    private Double taxaFrete;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String observacao;

    public Romaneio() {
    }


    public Double getValorTotal() {
        Double soma = 0.0;
        for (Pedido pedido : pedidos) {
            soma += pedido.getValorTotal();
        }
        return soma;
    }

    public Double getPesoTotal() {
        Double soma = 0.0;
        for (Pedido pedido : pedidos) {
            soma += pedido.getPesoTotal();
        }
        return soma;
    }

    public Romaneio(Long id, List<Pedido> pedidos, Transportador transportador, StatusRomaneio status, Double taxaFrete, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, String observacao) {
        this.id = id;
        this.pedidos = pedidos;
        this.transportador = transportador;
        Status = status;
        this.taxaFrete = taxaFrete;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public Transportador getTransportador() {
        return transportador;
    }

    public void setTransportador(Transportador transportador) {
        this.transportador = transportador;
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

