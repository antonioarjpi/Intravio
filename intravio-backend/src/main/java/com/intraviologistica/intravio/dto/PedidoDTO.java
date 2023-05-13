package com.intraviologistica.intravio.dto;

import com.intraviologistica.intravio.model.Pedido;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoDTO {

    private String id;
    private Integer numeroPedido;
    private Integer statusPedido;
    private Integer numeroRomaneio;
    private String remetenteNome;
    private String remetenteEmail;
    private String destinatarioNome;
    private String destinatarioEmail;
    private String origem;
    private String destino;
    private LocalDateTime dataPedido;
    private LocalDateTime dataAtualizacao;
    private Integer prioridade;
    private Integer acompanhaStatus;
    private String codigoRastreio;
    private Double pesoPedido;
    private Double valorPedido;
    private List<ItemDTO> itens = new ArrayList<>();
    private List<String> imagens = new ArrayList<>();

    public PedidoDTO() {
    }

    public PedidoDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.itens = pedido.getItens().stream().map(x -> new ItemDTO(x)).collect(Collectors.toList());
        this.imagens = pedido.getImagens();
        this.numeroRomaneio = pedido.getRomaneio() == null ? null : pedido.getRomaneio().getNumeroRomaneio();
        this.statusPedido = pedido.getStatusPedido().ordinal();
        this.remetenteNome = pedido.getRemetente().getNome();
        this.remetenteEmail = pedido.getRemetente().getEmail();
        this.destinatarioNome = pedido.getDestinatario().getNome();
        this.destinatarioEmail = pedido.getDestinatario().getEmail();
        this.origem = pedido.getOrigem().getNome();
        this.destino = pedido.getDestino().getNome();
        this.dataPedido = pedido.getDataPedido();
        this.dataAtualizacao = pedido.getDataAtualizacao();
        this.prioridade = pedido.getPrioridade().ordinal();
        this.acompanhaStatus = pedido.getAcompanhaStatus().ordinal();
        this.pesoPedido = pedido.getPesoTotal();
        this.valorPedido = pedido.getValorTotal();
        this.codigoRastreio = pedido.getCodigoRastreio();
        this.numeroPedido = pedido.getNumeroPedido();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ItemDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemDTO> itens) {
        this.itens = itens;
    }

    public Integer getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(Integer statusPedido) {
        this.statusPedido = statusPedido;
    }

    public String getRemetenteNome() {
        return remetenteNome;
    }

    public void setRemetenteNome(String remetenteNome) {
        this.remetenteNome = remetenteNome;
    }

    public String getRemetenteEmail() {
        return remetenteEmail;
    }

    public void setRemetenteEmail(String remetenteEmail) {
        this.remetenteEmail = remetenteEmail;
    }

    public String getDestinatarioNome() {
        return destinatarioNome;
    }

    public void setDestinatarioNome(String destinatarioNome) {
        this.destinatarioNome = destinatarioNome;
    }

    public String getDestinatarioEmail() {
        return destinatarioEmail;
    }

    public void setDestinatarioEmail(String destinatarioEmail) {
        this.destinatarioEmail = destinatarioEmail;
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

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }

    public Integer getAcompanhaStatus() {
        return acompanhaStatus;
    }

    public void setAcompanhaStatus(Integer acompanhaStatus) {
        this.acompanhaStatus = acompanhaStatus;
    }

    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    public void setCodigoRastreio(String codigoRastreio) {
        this.codigoRastreio = codigoRastreio;
    }

    public Double getPesoPedido() {
        return pesoPedido;
    }

    public void setPesoPedido(Double pesoPedido) {
        this.pesoPedido = pesoPedido;
    }

    public Double getValorPedido() {
        return valorPedido;
    }

    public void setValorPedido(Double valorPedido) {
        this.valorPedido = valorPedido;
    }

    public List<String> getImagens() {
        return imagens;
    }

    public void setImagens(List<String> imagens) {
        this.imagens = imagens;
    }

    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(Integer numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Integer getNumeroRomaneio() {
        return numeroRomaneio;
    }

    public void setNumeroRomaneio(Integer numeroRomaneio) {
        this.numeroRomaneio = numeroRomaneio;
    }
}
