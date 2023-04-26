package com.intraviologistica.intravio.controller;

import com.intraviologistica.intravio.dto.FuncionarioDTO;
import com.intraviologistica.intravio.service.FuncionarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioDTO>> listarFuncionarios() {
        List<FuncionarioDTO> funcionarios = funcionarioService.listaFuncionarios();
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> buscarFuncionarioPorId(@PathVariable Long id) {
        FuncionarioDTO funcionario = funcionarioService.buscaFuncionarioPorId(id);
        return ResponseEntity.ok(funcionario);
    }

    @PostMapping
    public ResponseEntity<FuncionarioDTO> salvaFuncionario(@RequestBody FuncionarioDTO funcionarioDTO) {
        FuncionarioDTO savedFuncionario = funcionarioService.salvaFuncionario(funcionarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFuncionario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> atualizaFuncionario(@PathVariable Long id, @RequestBody FuncionarioDTO funcionarioDTO) {
        FuncionarioDTO updatedFuncionario = funcionarioService.atualizaFuncionario(id, funcionarioDTO);
        return ResponseEntity.ok(updatedFuncionario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletaFuncionario(@PathVariable Long id) {
        funcionarioService.deletarFuncionario(id);
        return ResponseEntity.noContent().build();
    }
}
