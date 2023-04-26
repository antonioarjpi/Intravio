package com.intraviologistica.intravio.dto;

import com.intraviologistica.intravio.model.Item;
import com.intraviologistica.intravio.model.enums.AcompanhaStatus;
import com.intraviologistica.intravio.model.enums.Prioridade;
import com.intraviologistica.intravio.model.enums.StatusPedido;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDTO {

    private Long id;
    private List<Item> itens = new ArrayList<>();
    private List<String> fotos = new ArrayList<>();
    private StatusPedido statusPedido;
    private String remetente;
    private String destinatario;
    private String origem;
    private String destino;
    private LocalDateTime dataPedido;
    private LocalDateTime dataAtualizacao;
    private Prioridade prioridade;
    private AcompanhaStatus acompanhaStatus;

    public PedidoDTO() {
    }

    public PedidoDTO(Long id, List<Item> itens, List<String> fotos, StatusPedido statusPedido, String remetente, String destinatario, String origem, String destino, LocalDateTime dataPedido, LocalDateTime dataAtualizacao, Prioridade prioridade) {
        this.id = id;
        this.itens = itens;
        this.fotos = fotos;
        this.statusPedido = statusPedido;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.origem = origem;
        this.destino = destino;
        this.dataPedido = dataPedido;
        this.prioridade = prioridade;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public StatusPedido getStatus() {
        return statusPedido;
    }

    public void setStatus(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public StatusPedido getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }

    public AcompanhaStatus getAcompanhaStatus() {
        return acompanhaStatus;
    }

    public void setAcompanhaStatus(AcompanhaStatus acompanhaStatus) {
        this.acompanhaStatus = acompanhaStatus;
    }
}
