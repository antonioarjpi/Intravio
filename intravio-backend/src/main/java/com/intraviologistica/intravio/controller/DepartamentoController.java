package com.intraviologistica.intravio.controller;

import com.intraviologistica.intravio.dto.DepartamentoDTO;
import com.intraviologistica.intravio.model.Departamento;
import com.intraviologistica.intravio.service.DepartamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/departamentos")
public class DepartamentoController {

    private DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @GetMapping
    public ResponseEntity<List<DepartamentoDTO>> listarDepartamentos() {
        return ResponseEntity.ok(departamentoService.listarDepartamentos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoDTO> buscarDepartamentoPorId(@PathVariable String id) {
        return ResponseEntity.ok(departamentoService.buscarDepartamentoPorIdRetornandoDTO(id));
    }

    @PostMapping
    public ResponseEntity<Departamento> salvarDepartamento(@RequestBody @Valid DepartamentoDTO departamento) {
        Departamento departamentoSalvo = departamentoService.salvarDepartamento(departamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(departamentoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Departamento> atualizarDepartamento(@PathVariable String id, @RequestBody @Valid DepartamentoDTO departamentoAtualizado) {
        return ResponseEntity.ok(departamentoService.atualizarDepartamento(id, departamentoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirDepartamento(@PathVariable String id) {
        departamentoService.excluirDepartamento(id);
        return ResponseEntity.noContent().build();
    }
}