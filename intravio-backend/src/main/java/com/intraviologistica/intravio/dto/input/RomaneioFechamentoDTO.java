package com.intraviologistica.intravio.dto.input;

import java.util.List;

public class RomaneioFechamentoDTO {

    private String romaneioId;
    private List<Integer> pedidosConcluido;
    private List<Integer> pedidosRetorno;

    public RomaneioFechamentoDTO() {
    }

    public String getRomaneioId() {
        return romaneioId;
    }

    public void setRomaneioId(String romaneioId) {
        this.romaneioId = romaneioId;
    }

    public List<Integer> getPedidosConcluido() {
        return pedidosConcluido;
    }

    public void setPedidosConcluido(List<Integer> pedidosConcluido) {
        this.pedidosConcluido = pedidosConcluido;
    }

    public List<Integer> getPedidosRetorno() {
        return pedidosRetorno;
    }

    public void setPedidosRetorno(List<Integer> pedidosRetorno) {
        this.pedidosRetorno = pedidosRetorno;
    }
}
