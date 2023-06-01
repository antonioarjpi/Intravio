package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.UsuarioDTO;
import com.intraviologistica.intravio.dto.input.AlterarSenhaDTO;
import com.intraviologistica.intravio.dto.input.UsuarioInputDTO;
import com.intraviologistica.intravio.model.Usuario;
import com.intraviologistica.intravio.model.enums.Perfil;
import com.intraviologistica.intravio.repository.UsuarioRepository;
import com.intraviologistica.intravio.security.JwtService;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import com.intraviologistica.intravio.service.exceptions.RuleOfBusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        Usuario usuario = getUsuario();
        UsuarioInputDTO dto = getUsuarioInput();

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario); // Simule o comportamento do repository para retornar o usuário simulado
        when(passwordEncoder.encode(dto.getSenha())).thenReturn("encoded_password"); // Simule o comportamento do passwordEncoder para retornar uma senha codificada
        when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario));
    }

    @Test
    public void testCadastrarUsuario_QuandoSucesso() {
        UsuarioInputDTO dto = getUsuarioInput();

        Usuario usuarioSalvo = getUsuario();

        UsuarioDTO resultado = usuarioService.cadastrarUsuario(dto); // Chame o método do serviço para cadastrar um usuário

        // Verifique se o usuário retornado tem os dados esperados
        assertEquals(usuarioSalvo.getId(), resultado.getId());
        assertEquals(usuarioSalvo.getPrimeiroNome(), resultado.getPrimeiroNome());
        assertEquals(usuarioSalvo.getSegundoNome(), resultado.getSegundoNome());
        assertEquals(usuarioSalvo.getEmail(), resultado.getEmail());

        verify(usuarioRepository, times(1)).save(any(Usuario.class)); // Verifique se o método save() do repository foi chamado uma vez com o usuário correto

        verify(passwordEncoder, times(1)).encode(dto.getSenha()); // Verifique se o método encode() do passwordEncoder foi chamado uma vez com a senha correta
    }

    @Test
    public void testCadastrarUsuario_ErroQyabdiEmailJaExistente() {
        String email = "usuario@example.com";

        UsuarioInputDTO dto = new UsuarioInputDTO();
        dto.setEmail(email);

        when(usuarioRepository.existsByEmail(email)).thenReturn(true); // Simule o comportamento do método existsByEmail() do repository para retornar true

        Assertions.assertThrows(RuleOfBusinessException.class,
                () -> usuarioService.cadastrarUsuario(dto)); // Chame o método do serviço para cadastrar um usuário e verifique se uma exceção é lançada

        verify(usuarioRepository, times(1)).existsByEmail(email);  // Verifique se o método existsByEmail() do repository foi chamado uma vez com o e-mail correto

        verify(usuarioRepository, never()).save(any(Usuario.class)); // Verifique se o método save() do repository não foi chamado
    }

    @Test
    public void testEncontraUsuarioPorEmail_UsuarioExistente() {
        String email = "antonio@email.com";

        Usuario usuario = getUsuario();

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario)); // Simule o comportamento do repository para retornar o usuário simulado

        Usuario resultado = usuarioService.encontraUsuarioPorEmail(email); // Chame o método do serviço para encontrar um usuário por email

        assertEquals(usuario, resultado);    // Verifique se o usuário retornado é igual ao usuário simulado

        verify(usuarioRepository, times(1)).findByEmail(email); // Verifique se o método findByEmail() do repository foi chamado uma vez com o email correto
    }

    @Test
    public void testEncontraUsuarioPorEmail_UsuarioNaoEncontrado() {
        String email = "antonio@email.com";

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty()); // Simule o comportamento do repository para retornar Optional.empty()

        assertThrows(ResourceNotFoundException.class,
                () -> usuarioService.encontraUsuarioPorEmail(email)); // Chame o método do serviço para encontrar um usuário por email

        verify(usuarioRepository, times(1)).findByEmail(email); // Verifique se o método findByEmail() do repository foi chamado uma vez com o email correto
    }

    @Test
    public void testListarUsuários() {
        Usuario usuario1 = getUsuario();

        List<Usuario> usuariosSimulados = Arrays.asList(usuario1);

        when(usuarioRepository.findAll()).thenReturn(usuariosSimulados); // Simule o comportamento do repository para retornar a lista simulada de usuários

        List<UsuarioDTO> resultado = usuarioService.listarUsuários();  // Chame o método do serviço para listar usuários

        // Verifique se a lista de usuários retornada está correta
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals(usuario1.getId(), resultado.get(0).getId());
        Assertions.assertEquals(usuario1.getPrimeiroNome(), resultado.get(0).getPrimeiroNome());
        Assertions.assertEquals(usuario1.getSegundoNome(), resultado.get(0).getSegundoNome());
        Assertions.assertEquals(usuario1.getEmail(), resultado.get(0).getEmail());

        verify(usuarioRepository, times(1)).findAll(); // Verifique se o método findAll() do repository foi chamado uma vez
    }

    @Test
    public void testEncontraUsuarioPorId_UsuarioExistente() {
        String id = "1";
        Usuario usuario = getUsuario();
        UsuarioDTO resultado = usuarioService.encontraUsuarioPorId(id);

        // Verifique se o usuário retornado é igual ao usuário simulado
        Assertions.assertEquals(usuario.getId(), resultado.getId());
        Assertions.assertEquals(usuario.getPrimeiroNome(), resultado.getPrimeiroNome());
        Assertions.assertEquals(usuario.getSegundoNome(), resultado.getSegundoNome());
        Assertions.assertEquals(usuario.getEmail(), resultado.getEmail());

        verify(usuarioRepository, times(1)).findById(id); // Verifique se o método findById() do repository foi chamado uma vez com o ID correto
    }

    @Test
    public void testEncontraUsuarioPorId_UsuarioNaoEncontrado() {
        String id = "1";

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> usuarioService.encontraUsuarioPorId(id)); // Chame o método do serviço para encontrar um usuário por ID

        verify(usuarioRepository, times(1)).findById(id);// Verifique se o método findById() do repository foi chamado uma vez com o ID correto
    }

    @Test
    public void testFindById_UsuarioExistente() {
        String id = "1";

        Usuario usuario = getUsuario();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.findById(id);

        Assertions.assertEquals(usuario, resultado);

        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    public void testFindById_UsuarioNaoEncontrado() {
        String id = "1";

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> usuarioService.findById(id));

        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    public void testAtualizaUsuario_UsuarioExistente() {
        String id = "1";
        UsuarioInputDTO dto = getUsuarioInput();

        Usuario resultado = usuarioService.atualizaUsuario(id, dto);

        verify(usuarioRepository, times(1)).findById(id); // Verifique se o método findById() do repository foi chamado uma vez com o ID correto

        // Verifique se o objeto Usuario foi atualizado corretamente com os dados do DTO
        Assertions.assertEquals(id, resultado.getId());
        Assertions.assertEquals(dto.getPrimeiroNome(), resultado.getPrimeiroNome());
        Assertions.assertEquals(dto.getSegundoNome(), resultado.getSegundoNome());
        Assertions.assertEquals(dto.getEmail(), resultado.getEmail());
        Assertions.assertEquals(dto.getPerfil(), resultado.getPerfil());
    }

    @Test
    public void testAtualizaUsuario_UsuarioNaoEncontrado() {
        String id = "1";
        UsuarioInputDTO dto = getUsuarioInput();

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> usuarioService.atualizaUsuario(id, dto));

        // Verifique se o método findById() do repository foi chamado uma vez com o ID correto
        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    public void testDeletaUsuario_UsuarioExistente() {
        String id = "1";

        usuarioService.deletaUsuario(id);

        verify(usuarioRepository, times(1)).findById(id); // Verifique se o método findById() do serviço foi chamado uma vez com o ID correto

        verify(usuarioRepository, times(1)).deleteById(id); // Verifique se o método deleteById() do repository foi chamado uma vez com o ID correto
    }

    @Test
    public void testDeletaUsuario_UsuarioNaoEncontrado() {
        String id = "1";

        when(usuarioService.findById(id)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.findById(id)); // Verifique se foi lançada a exceção "ResourceNotFoundException"

        verify(usuarioRepository, times(1)).findById(id); // Verifique se o método findById() do serviço foi chamado uma vez com o ID correto

        verify(usuarioRepository, never()).deleteById(id); // Verifique se o método deleteById() do repository não foi chamado
    }

    @Test
    public void testAlterarSenha_SenhaCorreta() {
        String id = "1";
        String senhaAtual = "senha";
        String senhaNova = "senhaNova";

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);  // Simule o comportamento do método matches() do passwordEncoder para retornar true

        usuarioService.alterarSenha(id, new AlterarSenhaDTO(senhaAtual, senhaNova)); // Chame o método do serviço para alterar a senha

        verify(usuarioRepository, times(1)).findById(id); // Verifique se o método findById() do serviço foi chamado uma vez com o ID correto

        verify(passwordEncoder, times(1)).matches(anyString(), anyString()); // Verifique se o método matches() do passwordEncoder foi chamado uma vez com as senhas corretas

        verify(usuarioRepository, times(1)).save(any(Usuario.class));   // Verifique se o método save() do repository foi chamado uma vez com o usuário atualizado

        verify(passwordEncoder, times(1)).encode(senhaNova); // Verifique se o método encode() do passwordEncoder foi chamado uma vez com a senha correta
    }

    @Test
    public void testAlterarSenha_SenhaIncorreta() {
        String id = "1";
        String senhaAtual = "senhaAntigaIncorreta";
        String senhaNova = "senhaNova";

        AlterarSenhaDTO senhas = new AlterarSenhaDTO();
        senhas.setSenhaAtual(senhaAtual);
        senhas.setSenhaNova(senhaNova);

        when(passwordEncoder.matches(senhaAtual, getUsuario().getSenha())).thenReturn(false); // Simule o comportamento do método matches() do passwordEncoder para retornar false

        assertThrows(RuleOfBusinessException.class,
                () -> usuarioService.alterarSenha(id, new AlterarSenhaDTO(senhaAtual, senhaNova))); // Chame o método do serviço para alterar a senha e verifique se uma exceção é lançada

        verify(usuarioRepository, times(1)).findById(id);  // Verifique se o método findById() do serviço foi chamado uma vez com o ID correto

        verify(passwordEncoder, times(1)).matches(senhaAtual, getUsuario().getSenha()); // Verifique se o método matches() do passwordEncoder foi chamado uma vez com as senhas corretas

        verify(usuarioRepository, never()).save(any(Usuario.class)); // Verifique se o método save() do repository não foi chamado
    }

    private static Usuario getUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId("1");
        usuario.setPrimeiroNome("Antônio");
        usuario.setSegundoNome("Sousa");
        usuario.setEmail("antonio@email.com");
        usuario.setSenha("password");
        usuario.setPerfil(Perfil.STANDARD);
        return usuario;
    }

    private static UsuarioInputDTO getUsuarioInput() {
        UsuarioInputDTO usuario = new UsuarioInputDTO();
        usuario.setId("1");
        usuario.setPrimeiroNome("Antônio");
        usuario.setSegundoNome("Sousa");
        usuario.setEmail("antonio@email.com");
        usuario.setSenha("senha");
        usuario.setPerfil(Perfil.STANDARD);
        return usuario;
    }
}