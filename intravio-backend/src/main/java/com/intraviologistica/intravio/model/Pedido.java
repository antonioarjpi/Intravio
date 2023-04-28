package com.intraviologistica.intravio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intraviologistica.intravio.model.enums.AcompanhaStatus;
import com.intraviologistica.intravio.model.enums.Prioridade;
import com.intraviologistica.intravio.model.enums.StatusPedido;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pedido {

    @Id
    private String id;

    @Column(unique = true)
    private Integer numeroPedido;

    @OneToMany(mappedBy = "id.pedido", cascade = CascadeType.ALL)
    private List<Item> itens = new ArrayList<>();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<HistoricoPedido> historicoPedidos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "remetente_id")
    private Funcionario remetente;

    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    private Funcionario destinatario;

    @ManyToOne
    @JoinColumn(name = "origem_id")
    private Filial origem;

    @ManyToOne
    @JoinColumn(name = "destino_id")
    private Filial destino;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "romaneio_id")
    private Romaneio romaneio;

    @ElementCollection
    private List<String> imagens = new ArrayList<>();
    private LocalDateTime dataPedido = LocalDateTime.now();
    private LocalDateTime dataAtualizacao;
    private String codigoRastreio;
    private AcompanhaStatus acompanhaStatus;
    private StatusPedido statusPedido;
    private Prioridade prioridade;

    public Pedido() {
    }

    public Pedido(String id, List<Item> itens, List<String> imagens, Funcionario remetente, Funcionario destinatario, Filial origem, Filial destino, LocalDateTime dataPedido, LocalDateTime dataAtualizacao, String codigoRastreio, AcompanhaStatus acompanhaStatus, StatusPedido statusPedido, Prioridade prioridade, Romaneio romaneio) {
        this.id = id;
        this.itens = itens;
        this.imagens = imagens;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.origem = origem;
        this.destino = destino;
        this.dataPedido = dataPedido;
        this.dataAtualizacao = dataAtualizacao;
        this.codigoRastreio = codigoRastreio;
        this.acompanhaStatus = acompanhaStatus;
        this.statusPedido = statusPedido;
        this.prioridade = prioridade;
        this.romaneio = romaneio;
    }

    public Double getValorTotal() {
        Double soma = 0.0;
        for (Item item : itens) {
            soma += item.getPrecoTotal();
        }
        return soma;
    }

    public Double getPesoTotal() {
        Double soma = 0.0;
        for (Item item : itens) {
            soma += item.getPesoTotal();
        }
        return soma;
    }

    public void atualizarStatus(StatusPedido novoStatus, String comentario) {
        StatusPedido statusAnterior = this.statusPedido;
        this.statusPedido = novoStatus;
        HistoricoPedido historico = new HistoricoPedido();
        historico.setPedido(this);
        historico.setDataAtualizacao(LocalDateTime.now());
        historico.setStatusAnterior(statusAnterior);
        historico.setStatusAtual(novoStatus);
        historico.setComentario(comentario);
        this.historicoPedidos.add(historico);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }

    public List<String> getImagens() {
        return imagens;
    }

    public void setImagens(List<String> imagens) {
        this.imagens = imagens;
    }

    public StatusPedido getStatus() {
        return statusPedido;
    }

    public void setStatus(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }

    public Funcionario getRemetente() {
        return remetente;
    }

    public void setRemetente(Funcionario remetente) {
        this.remetente = remetente;
    }

    public Funcionario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Funcionario destinatario) {
        this.destinatario = destinatario;
    }

    public Filial getOrigem() {
        return origem;
    }

    public void setOrigem(Filial origem) {
        this.origem = origem;
    }

    public Filial getDestino() {
        return destino;
    }

    public void setDestino(Filial destino) {
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

    public AcompanhaStatus getAcompanhaStatus() {
        return acompanhaStatus;
    }

    public void setAcompanhaStatus(AcompanhaStatus acompanhaStatus) {
        this.acompanhaStatus = acompanhaStatus;
    }

    public Romaneio getRomaneio() {
        return romaneio;
    }

    public void setRomaneio(Romaneio romaneio) {
        this.romaneio = romaneio;
    }

    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    public void setCodigoRastreio(String codigoRastreio) {
        this.codigoRastreio = codigoRastreio;
    }

    public StatusPedido getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }

    public List<HistoricoPedido> getHistoricoPedidos() {
        return historicoPedidos;
    }

    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(Integer numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", numeroPedido=" + numeroPedido +
                ", itens=" + itens +
                ", historicoPedidos=" + historicoPedidos +
                ", remetente=" + remetente +
                ", destinatario=" + destinatario +
                ", origem=" + origem +
                ", destino=" + destino +
                ", romaneio=" + romaneio +
                ", imagens=" + imagens +
                ", dataPedido=" + dataPedido +
                ", dataAtualizacao=" + dataAtualizacao +
                ", codigoRastreio='" + codigoRastreio + '\'' +
                ", acompanhaStatus=" + acompanhaStatus +
                ", statusPedido=" + statusPedido +
                ", prioridade=" + prioridade +
                '}';
    }
}
