package com.intraviologistica.intravio.dto.input;

import com.intraviologistica.intravio.model.enums.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioInputDTO {

    private String id;
    @NotBlank(message = "Preenchimento de nome é obrigatório")
    private String primeiroNome;
    private String segundoNome;
    @Email(message = "Digite um e-mail válido")
    private String email;
    @NotBlank(message = "Preenchimento de senha é obrigatório")
    private String senha;
    @NotBlank(message = "Preenchimento de perfil é obrigatório")
    private Perfil perfil;

    public UsuarioInputDTO() {
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getSegundoNome() {
        return segundoNome;
    }

    public void setSegundoNome(String segundoNome) {
        this.segundoNome = segundoNome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
