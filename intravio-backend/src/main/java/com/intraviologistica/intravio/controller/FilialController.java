package com.intraviologistica.intravio.controller;

import com.intraviologistica.intravio.dto.FilialDTO;
import com.intraviologistica.intravio.model.Filial;
import com.intraviologistica.intravio.service.FilialService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/filiais")
public class FilialController {

    private final FilialService filialService;

    public FilialController(FilialService filialService) {
        this.filialService = filialService;
    }

    @GetMapping
    public ResponseEntity<List<FilialDTO>> listarFiliais() {
        List<FilialDTO> filiais = filialService.listarFiliais();
        return ResponseEntity.ok(filiais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilialDTO> buscarPorId(@PathVariable Long id) {
        FilialDTO filialDTO = filialService.buscarFilialPorId(id);
        return ResponseEntity.ok(filialDTO);
    }

    @PostMapping
    public ResponseEntity<Filial> salvaFilial(@RequestBody @Valid FilialDTO filialDTO, HttpServletResponse response) {
        Filial filialSalva = filialService.salvaFilial(filialDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(filialSalva.getId()).toUri();
        response.setHeader("Location", uri.toASCIIString());
        return ResponseEntity.created(uri).body(filialSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Filial> atualizaFilial(@PathVariable Long id, @RequestBody @Valid FilialDTO filialDTO) {
        filialDTO.setId(id);
        Filial filialAtualizada = filialService.atualizaFilial(filialDTO);
        return ResponseEntity.ok(filialAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluiFilial(@PathVariable Long id) {
        filialService.excluiFilial(id);
        return ResponseEntity.noContent().build();
    }
}
