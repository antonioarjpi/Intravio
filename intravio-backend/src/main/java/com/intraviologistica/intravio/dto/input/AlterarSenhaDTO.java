package com.intraviologistica.intravio.dto.input;

public class AlterarSenhaDTO {

    String senhaAtual;
    String senhaNova;

    public AlterarSenhaDTO() {
    }

    public AlterarSenhaDTO(String senhaAtual, String senhaNova) {
        this.senhaAtual = senhaAtual;
        this.senhaNova = senhaNova;
    }

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }
}
