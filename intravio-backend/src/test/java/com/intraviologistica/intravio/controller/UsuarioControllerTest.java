package com.intraviologistica.intravio.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intraviologistica.intravio.dto.CredenciaisDTO;
import com.intraviologistica.intravio.dto.TokenDTO;
import com.intraviologistica.intravio.dto.UsuarioDTO;
import com.intraviologistica.intravio.dto.input.AlterarSenhaDTO;
import com.intraviologistica.intravio.dto.input.UsuarioInputDTO;
import com.intraviologistica.intravio.model.Usuario;
import com.intraviologistica.intravio.model.enums.Perfil;
import com.intraviologistica.intravio.service.UsuarioService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
    }

    @Test
    public void testCadastraUsuario() throws Exception {
        UsuarioInputDTO usuarioInputDTO = getUsuarioInputDTO();

        UsuarioDTO usuarioDTO = getUsuarioDTO();

        // Mock do serviço
        Mockito.when(usuarioService.cadastrarUsuario(any(UsuarioInputDTO.class)))
                .thenReturn(usuarioDTO);

        // Executa a requisição POST e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/usuarios/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoParaJson(usuarioInputDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(usuarioDTO.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.primeiroNome", Matchers.is(usuarioDTO.getPrimeiroNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.segundoNome", Matchers.is(usuarioDTO.getSegundoNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(usuarioDTO.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.perfil", Matchers.is(usuarioDTO.getPerfil().toString())));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(usuarioService, Mockito.times(1)).cadastrarUsuario(any(UsuarioInputDTO.class));
        Mockito.verifyNoMoreInteractions(usuarioService);
    }

    @Test
    public void testFazerLogin() throws Exception {
        CredenciaisDTO dto = new CredenciaisDTO("usuario@email.com", "senha123");
        dto.setEmail("usuario@email.com");
        dto.setSenha("senha123");

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken("ey1uhekiasui123ikdas.ki123ijs123as98asj12");

        // Mock do serviço
        Mockito.when(usuarioService.fazerLogin(any(CredenciaisDTO.class)))
                .thenReturn(tokenDTO);

        // Executa a requisição POST e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/usuarios/autenticar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Authorization", "Bearer " + tokenDTO.getToken()))
                .andExpect(MockMvcResultMatchers.header().exists("access-control-expose-headers"))
                .andExpect(MockMvcResultMatchers.header().exists("Authorization"))
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(tokenDTO)));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(usuarioService, Mockito.times(1)).fazerLogin(any(CredenciaisDTO.class));
        Mockito.verifyNoMoreInteractions(usuarioService);
    }

    @Test
    public void testListarTodosUsuarios() throws Exception {
        UsuarioDTO usuario1 = getUsuarioDTO();

        UsuarioDTO usuario2 = getUsuarioDTO2();

        List<UsuarioDTO> listaUsuarios = Arrays.asList(usuario1, usuario2);

        // Mock do serviço
        Mockito.when(usuarioService.listarUsuários())
                .thenReturn(listaUsuarios);

        // Executa a requisição GET e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/usuarios"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(usuario1.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].primeiroNome", Matchers.is(usuario1.getPrimeiroNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].segundoNome", Matchers.is(usuario1.getSegundoNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is(usuario1.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].perfil", Matchers.is(usuario1.getPerfil().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(usuario2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].primeiroNome", Matchers.is(usuario2.getPrimeiroNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].segundoNome", Matchers.is(usuario2.getSegundoNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email", Matchers.is(usuario2.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].perfil", Matchers.is(usuario2.getPerfil().toString())));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(usuarioService, Mockito.times(1)).listarUsuários();
        Mockito.verifyNoMoreInteractions(usuarioService);
    }

    @Test
    public void testEncontraUsuarioPorId() throws Exception {
        String id = "1";
        UsuarioDTO usuario = getUsuarioDTO();

        // Mock do serviço
        Mockito.when(usuarioService.encontraUsuarioPorId(id))
                .thenReturn(usuario);

        // Executa a requisição GET e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/usuarios/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(usuario.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.primeiroNome", Matchers.is(usuario.getPrimeiroNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.segundoNome", Matchers.is(usuario.getSegundoNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(usuario.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.perfil", Matchers.is(usuario.getPerfil().toString())));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(usuarioService, Mockito.times(1)).encontraUsuarioPorId(id);
        Mockito.verifyNoMoreInteractions(usuarioService);
    }

    @Test
    public void testAtualizarUsuario() throws Exception {
        String id = "1";
        UsuarioInputDTO usuarioInputDTO = getUsuarioInputDTO();

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(id);
        usuarioAtualizado.setPrimeiroNome(usuarioInputDTO.getPrimeiroNome());
        usuarioAtualizado.setSegundoNome(usuarioInputDTO.getSegundoNome());
        usuarioAtualizado.setEmail(usuarioInputDTO.getEmail());
        usuarioAtualizado.setSenha(usuarioInputDTO.getSenha());
        usuarioAtualizado.setPerfil(Perfil.STANDARD);

        // Mock do serviço
        Mockito.when(usuarioService.atualizaUsuario(eq(id), any(UsuarioInputDTO.class)))
                .thenReturn(usuarioAtualizado);

        // Executa a requisição PUT e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(converteObjetoParaJson(usuarioInputDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(usuarioAtualizado.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.primeiroNome", Matchers.is(usuarioAtualizado.getPrimeiroNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.segundoNome", Matchers.is(usuarioAtualizado.getSegundoNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(usuarioAtualizado.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.perfil", Matchers.is(usuarioAtualizado.getPerfil().toString())));

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(usuarioService, Mockito.times(1)).atualizaUsuario(eq(id), any(UsuarioInputDTO.class));
        Mockito.verifyNoMoreInteractions(usuarioService);
    }

    @Test
    public void testDeletarUsuario() throws Exception {
        String id = "1";

        // Executa a requisição DELETE e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/usuarios/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(usuarioService, Mockito.times(1)).deletaUsuario(id);
        Mockito.verifyNoMoreInteractions(usuarioService);
    }

    @Test
    public void testAtualizarSenha() throws Exception {
        String id = "1";
        AlterarSenhaDTO senhaDTO = new AlterarSenhaDTO();
        senhaDTO.setSenhaAtual("senhaAntiga");
        senhaDTO.setSenhaNova("novaSenha");

        // Executa a requisição PUT e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/usuarios/{id}/alterar-senha", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(senhaDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verifica se o método do serviço foi chamado corretamente
        Mockito.verify(usuarioService, Mockito.times(1)).alterarSenha(eq(id), any(AlterarSenhaDTO.class));
        Mockito.verifyNoMoreInteractions(usuarioService);
    }

    private static String converteObjetoParaJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    private static UsuarioDTO getUsuarioDTO2() {
        UsuarioDTO usuario2 = new UsuarioDTO();
        usuario2.setId("2");
        usuario2.setPrimeiroNome("Jane");
        usuario2.setSegundoNome("Smith");
        usuario2.setEmail("janesmith@example.com");
        usuario2.setPerfil(Perfil.ADMIN);
        return usuario2;
    }

    private static UsuarioInputDTO getUsuarioInputDTO() {
        UsuarioInputDTO usuarioInputDTO = new UsuarioInputDTO();
        usuarioInputDTO.setPrimeiroNome("John");
        usuarioInputDTO.setSegundoNome("Doe");
        usuarioInputDTO.setEmail("johndoe@example.com");
        usuarioInputDTO.setSenha("password");
        usuarioInputDTO.setPerfil(Perfil.STANDARD);
        return usuarioInputDTO;
    }

    private static UsuarioDTO getUsuarioDTO() {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId("1");
        usuario.setPrimeiroNome("John");
        usuario.setSegundoNome("Doe");
        usuario.setEmail("johndoe@example.com");
        usuario.setPerfil(Perfil.STANDARD);
        return usuario;
    }
}