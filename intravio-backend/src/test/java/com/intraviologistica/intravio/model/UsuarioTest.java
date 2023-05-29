package com.intraviologistica.intravio.model;

import com.intraviologistica.intravio.model.enums.Perfil;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsuarioTest {

    public static Usuario getUsuario(){
        Usuario usuario = new Usuario();
        usuario.setId("1234abcd");
        usuario.setPrimeiroNome("Arthur");
        usuario.setSegundoNome("Gomes");
        usuario.setEmail("arthur@gomes.com");
        usuario.setSenha("senha123");
        usuario.setPerfil(Perfil.STANDARD);
        return usuario;
    }

    @Test
    public void testGetAuthorities() {
        Usuario usuario = getUsuario();

        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("STANDARD")));
    }

    @Test
    public void testGetPassword() {
        Usuario usuario = getUsuario();

        String password = usuario.getPassword();

        assertEquals("senha123", password);
    }

    @Test
    public void testGetUsername() {
        Usuario usuario = getUsuario();

        String username = usuario.getUsername();

        assertEquals("arthur@gomes.com", username);
    }

    @Test
    public void testGetId() {
        Usuario usuario = getUsuario();

        String id = usuario.getId();

        assertEquals("1234abcd", id);
    }
}