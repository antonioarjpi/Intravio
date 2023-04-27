package com.intraviologistica.intravio.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.intraviologistica.intravio.model.HistoricoPedido;
import com.intraviologistica.intravio.model.enums.StatusPedido;

import java.time.LocalDateTime;

public class HistoricoPedidoDTO {

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataAtualizacao;
    private StatusPedido statusAnterior;
    private StatusPedido statusAtual;
    private String comentario;

    public HistoricoPedidoDTO() {
    }

    public HistoricoPedidoDTO(HistoricoPedido historicoPedido) {
        this.dataAtualizacao = historicoPedido.getDataAtualizacao();
        this.statusAnterior = historicoPedido.getStatusAnterior();
        this.statusAtual = historicoPedido.getStatusAtual();
        this.comentario = historicoPedido.getComentario();
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
}
