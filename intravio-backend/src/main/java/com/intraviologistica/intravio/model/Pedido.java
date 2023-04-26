package com.intraviologistica.intravio.model;

import com.intraviologistica.intravio.model.enums.AcompanhaStatus;
import com.intraviologistica.intravio.model.enums.Prioridade;
import com.intraviologistica.intravio.model.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "id.pedido")
    private List<Item> itens = new ArrayList<>();

    private List<String> fotos = new ArrayList<>();

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
    private LocalDateTime dataPedido = LocalDateTime.now();
    private LocalDateTime dataAtualizacao;

    @Enumerated(EnumType.STRING)
    private AcompanhaStatus acompanhaStatus;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    public Pedido() {
    }

    public Pedido(Long id, List<Item> itens, List<String> fotos, Status status, Funcionario remetente, Funcionario destinatario, Filial origem, Filial destino, LocalDateTime dataPedido, LocalDateTime dataAtualizacao, Prioridade prioridade, AcompanhaStatus acompanhaStatus) {
        this.id = id;
        this.itens = itens;
        this.fotos = fotos;
        this.status = status;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.origem = origem;
        this.destino = destino;
        this.dataPedido = dataPedido;
        this.prioridade = prioridade;
        this.dataAtualizacao = dataAtualizacao;
        this.acompanhaStatus = acompanhaStatus;
    }

    public Double getValorTotal(){
        Double soma = 0.0;
        for (Item item : itens){
            soma += item.getSubtotal();
        }
        return soma;
    }

    public Double getPesoTotal(){
        Double soma = 0.0;
        for (Item item : itens){
            soma += item.getPesoTotal();
        }
        return soma;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public LocalDateTime getDataAtualizacao(){
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao){
        this.dataAtualizacao = dataAtualizacao;
    }

    public AcompanhaStatus getAcompanhaStatus() {
        return acompanhaStatus;
    }

    public void setAcompanhaStatus(AcompanhaStatus acompanhaStatus) {
        this.acompanhaStatus = acompanhaStatus;
    }
}
