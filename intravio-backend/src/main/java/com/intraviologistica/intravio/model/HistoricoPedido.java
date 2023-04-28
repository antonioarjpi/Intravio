package com.intraviologistica.intravio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intraviologistica.intravio.model.enums.StatusPedido;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class HistoricoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    private LocalDateTime dataAtualizacao;
    private StatusPedido statusAnterior;
    private StatusPedido statusAtual;
    private String comentario;

    public HistoricoPedido() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public StatusPedido getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(StatusPedido statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public StatusPedido getStatusAtual() {
        return statusAtual;
    }

    public void setStatusAtual(StatusPedido statusAtual) {
        this.statusAtual = statusAtual;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "HistoricoPedido{" +
                "id=" + id +
                ", pedido=" + pedido +
                ", dataAtualizacao=" + dataAtualizacao +
                ", statusAnterior=" + statusAnterior +
                ", statusAtual=" + statusAtual +
                ", comentario='" + comentario + '\'' +
                '}';
    }
}