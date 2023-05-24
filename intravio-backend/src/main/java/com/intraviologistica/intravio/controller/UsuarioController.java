package com.intraviologistica.intravio.controller;

import com.intraviologistica.intravio.dto.CredenciaisDTO;
import com.intraviologistica.intravio.dto.TokenDTO;
import com.intraviologistica.intravio.dto.UsuarioDTO;
import com.intraviologistica.intravio.dto.input.AlterarSenhaDTO;
import com.intraviologistica.intravio.dto.input.UsuarioInputDTO;
import com.intraviologistica.intravio.model.Usuario;
import com.intraviologistica.intravio.service.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioDTO> cadastraUsuario(@RequestBody UsuarioInputDTO request) {
        return ResponseEntity.ok(usuarioService.cadastrarUsuario(request));
    }

    @PostMapping("/autenticar")
    public ResponseEntity<TokenDTO> fazerLogin(@RequestBody CredenciaisDTO dto, HttpServletResponse response) {
        TokenDTO tokenDTO = usuarioService.fazerLogin(dto);
        response.addHeader("Authorization", "Bearer " + tokenDTO.getToken());
        response.addHeader("access-control-expose-headers", "Authorization");
        return ResponseEntity.ok(tokenDTO);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodosUsuarios(){
        return ResponseEntity.ok(usuarioService.listarUsuários());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> encontraUsuarioPorId(@PathVariable String id){
        return ResponseEntity.ok(usuarioService.encontraUsuarioPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable String id, @RequestBody UsuarioInputDTO dto){
        return ResponseEntity.ok(usuarioService.atualizaUsuario(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> deletarUsuario(@PathVariable String id){
        usuarioService.deletaUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/alterar-senha")
    public ResponseEntity<Void> atualizarSenha(@PathVariable String id, @RequestBody AlterarSenhaDTO senhaDTO){
        usuarioService.alterarSenha(id, senhaDTO);
        return ResponseEntity.ok().build();
    }
}
