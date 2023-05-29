package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Usuario;
import com.intraviologistica.intravio.model.UsuarioTest;
import com.intraviologistica.intravio.model.enums.Perfil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testSalvarUsuario() {
        Usuario usuario = UsuarioTest.getUsuario();

        Usuario savedUsuario = usuarioRepository.save(usuario);

        assertNotNull(savedUsuario.getId());
        assertEquals("Arthur", savedUsuario.getPrimeiroNome());
        assertEquals("Gomes", savedUsuario.getSegundoNome());
        assertEquals("arthur@gomes.com", savedUsuario.getEmail());
        assertEquals("senha123", savedUsuario.getSenha());
        assertEquals(Perfil.STANDARD, savedUsuario.getPerfil());
    }

    @Test
    public void testAtualizarUsuario() {
        Usuario usuario = UsuarioTest.getUsuario();

        usuarioRepository.save(usuario);

        Usuario usuarioAtualizado = usuarioRepository.findById("1234abcd").orElse(null);
        assertNotNull(usuarioAtualizado);

        usuarioAtualizado.setPrimeiroNome("Diego");
        usuarioAtualizado.setSegundoNome("Torres");
        usuarioAtualizado.setEmail("diego@email.com");
        usuarioAtualizado.setSenha("abcd1234");
        usuarioAtualizado.setPerfil(Perfil.ADMIN);
        usuarioRepository.save(usuarioAtualizado);

        Usuario usuarioAtualizadoNovamente = usuarioRepository.findById("1234abcd").orElse(null);
        assertNotNull(usuarioAtualizadoNovamente);
        assertEquals("Diego", usuarioAtualizadoNovamente.getPrimeiroNome());
        assertEquals("Torres", usuarioAtualizadoNovamente.getSegundoNome());
        assertEquals("diego@email.com", usuarioAtualizadoNovamente.getEmail());
        assertEquals("abcd1234", usuarioAtualizadoNovamente.getSenha());
        assertEquals(Perfil.ADMIN, usuarioAtualizadoNovamente.getPerfil());
    }

    @Test
    public void testExcluirRomaneioPorId() {
        Usuario usuario = UsuarioTest.getUsuario();
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        assertThat(usuarioSalvo).isNotNull();

        String id = usuarioSalvo.getId();
        usuarioRepository.deleteById(id);

        Optional<Usuario> usuarioExcluido = usuarioRepository.findById(id);
        assertThat(usuarioExcluido.isPresent()).isFalse();
    }

    @Test
    void testListarTodosUsuarios() {
        Usuario usuario1 = UsuarioTest.getUsuario();
        usuario1 = usuarioRepository.save(usuario1);

        Usuario usuario2 = new Usuario("123abc", "Antônio", "José", "antonio@email.com", "123senha", Perfil.ADMIN);
        usuario2 = usuarioRepository.save(usuario2);

        List<Usuario> usuarios = usuarioRepository.findAll();

        assertFalse(usuarios.isEmpty());
        assertEquals(2, usuarios.size());
        assertTrue(usuarios.contains(usuario1));
        assertTrue(usuarios.contains(usuario2));
    }

    @Test
    void testBuscarPorId() {
        Usuario usuario = UsuarioTest.getUsuario();
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        assertThat(usuarioSalvo).isNotNull();

        String id = usuarioSalvo.getId();
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(id);

        assertThat(usuarioEncontrado)
                .isPresent()
                .get()
                .extracting(Usuario::getId)
                .isEqualTo(id);
    }

    @Test
    void findByEmail() {
        String email = "arthur@gomes.com";

        Usuario usuario = UsuarioTest.getUsuario();
        usuarioRepository.save(usuario);

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(email);

        assertThat(usuarioEncontrado).isPresent();
        assertThat(usuarioEncontrado.get().getId()).isNotNull();
        assertThat(usuarioEncontrado.get().getEmail()).isEqualTo(email);
    }

    @Test
    public void testExistsByEmail() {
        Usuario usuario = UsuarioTest.getUsuario();
        usuarioRepository.save(usuario);

        boolean exists = usuarioRepository.existsByEmail("arthur@gomes.com");

        Assertions.assertTrue(exists);
    }
}