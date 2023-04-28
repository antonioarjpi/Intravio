package com.intraviologistica.intravio.dto.input;

import com.intraviologistica.intravio.model.Item;
import com.intraviologistica.intravio.model.enums.AcompanhaStatus;
import com.intraviologistica.intravio.model.enums.Prioridade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoInputDTO {

    private String id;
    @NotNull(message = "A lista de itens não pode ser nula")
    @NotEmpty(message = "Não é possível criar pedido sem item")
    private List<Item> itens = new ArrayList<>();
    private List<String> fotos = new ArrayList<>();
    @NotBlank(message = "Preenchimento do Remetente é obrigatório")
    private String remetente;
    @NotBlank(message = "Preenchimento do Destinatario é obrigatório")
    private String destinatario;
    @NotBlank(message = "Preenchimento da Filial da Origem é obrigatório")
    private String origem;
    @NotBlank(message = "Preenchimento da Filial do Remetente é obrigatório")
    private String destino;
    private LocalDateTime dataPedido;
    private LocalDateTime dataAtualizacao;
    private Prioridade prioridade;
    private AcompanhaStatus acompanhaStatus;

    public PedidoInputDTO() {
    }

    public PedidoInputDTO(String id, List<Item> itens, List<String> fotos, String remetente, String destinatario, String origem, String destino, LocalDateTime dataPedido, LocalDateTime dataAtualizacao, Prioridade prioridade) {
        this.id = id;
        this.itens = itens;
        this.fotos = fotos;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.origem = origem;
        this.destino = destino;
        this.dataPedido = dataPedido;
        this.prioridade = prioridade;
        this.dataAtualizacao = dataAtualizacao;
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

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
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

    public AcompanhaStatus getAcompanhaStatus() {
        return acompanhaStatus;
    }

    public void setAcompanhaStatus(AcompanhaStatus acompanhaStatus) {
        this.acompanhaStatus = acompanhaStatus;
    }
}
