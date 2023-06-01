package com.intraviologistica.intravio.controller;

import com.intraviologistica.intravio.dto.FuncionarioDTO;
import com.intraviologistica.intravio.dto.input.FuncionarioInputDTO;
import com.intraviologistica.intravio.model.Funcionario;
import com.intraviologistica.intravio.service.FuncionarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/funcionarios")
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
    public ResponseEntity<FuncionarioDTO> buscarFuncionarioPorId(@PathVariable String id) {
        FuncionarioDTO funcionario = funcionarioService.buscaFuncionarioPorId(id);
        return ResponseEntity.ok(funcionario);
    }

    @PostMapping
    public ResponseEntity<FuncionarioDTO> salvaFuncionario(@RequestBody FuncionarioInputDTO funcionarioInputDTO) {
        FuncionarioDTO savedFuncionario = funcionarioService.salvaFuncionario(funcionarioInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFuncionario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> atualizaFuncionario(@PathVariable String id, @RequestBody FuncionarioInputDTO funcionarioInputDTO) {
        FuncionarioDTO updatedFuncionario = funcionarioService.atualizaFuncionario(id, funcionarioInputDTO);
        return ResponseEntity.ok(updatedFuncionario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletaFuncionario(@PathVariable String id) {
        funcionarioService.deletarFuncionario(id);
        return ResponseEntity.noContent().build();
    }
}
