package com.intraviologistica.intravio.dto;

import com.intraviologistica.intravio.model.Usuario;
import com.intraviologistica.intravio.model.enums.Perfil;

public class UsuarioDTO {

    private String id;
    private String primeiroNome;
    private String segundoNome;
    private String email;
    private Perfil perfil;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.primeiroNome = usuario.getPrimeiroNome();
        this.segundoNome = usuario.getSegundoNome();
        this.email = usuario.getEmail();
        this.perfil = usuario.getPerfil();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
