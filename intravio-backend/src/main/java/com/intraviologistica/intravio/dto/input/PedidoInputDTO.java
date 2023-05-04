package com.intraviologistica.intravio.dto.input;

import com.intraviologistica.intravio.model.Item;
import com.intraviologistica.intravio.model.Pedido;
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
    @NotNull(message = "Preenchimento da Filial da Origem é obrigatório")
    private Long origem;
    @NotNull(message = "Preenchimento da Filial do Remetente é obrigatório")
    private Long destino;
    private Integer numeroPedido;
    private LocalDateTime dataPedido;
    private LocalDateTime dataAtualizacao;
    private Prioridade prioridade;
    private AcompanhaStatus acompanhaStatus;

    public PedidoInputDTO() {
    }

    public PedidoInputDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.itens = pedido.getItens();
        this.fotos = pedido.getImagens();
        this.remetente = pedido.getRemetente().getId();
        this.destinatario = pedido.getDestinatario().getId();
        this.origem = pedido.getOrigem().getId();
        this.destino = pedido.getDestino().getId();
        this.dataPedido = pedido.getDataPedido();
        this.dataAtualizacao = pedido.getDataAtualizacao();
        this.prioridade = pedido.getPrioridade();
        this.acompanhaStatus = pedido.getAcompanhaStatus();
        this.numeroPedido = pedido.getNumeroPedido();
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

    public Long getOrigem() {
        return origem;
    }

    public void setOrigem(Long origem) {
        this.origem = origem;
    }

    public Long getDestino() {
        return destino;
    }

    public void setDestino(Long destino) {
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

    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(Integer numeroPedido) {
        this.numeroPedido = numeroPedido;
    }
}
