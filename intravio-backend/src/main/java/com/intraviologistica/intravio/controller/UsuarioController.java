package com.intraviologistica.intravio.controller;

import com.intraviologistica.intravio.dto.CredenciaisDTO;
import com.intraviologistica.intravio.dto.TokenDTO;
import com.intraviologistica.intravio.dto.UsuarioDTO;
import com.intraviologistica.intravio.dto.input.UsuarioInputDTO;
import com.intraviologistica.intravio.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<TokenDTO> fazerLogin(@RequestBody CredenciaisDTO dto) {
        return ResponseEntity.ok(usuarioService.fazerLogin(dto));
    }
}