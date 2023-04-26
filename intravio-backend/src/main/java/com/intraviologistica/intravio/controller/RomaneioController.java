package com.intraviologistica.intravio.controller;

import com.intraviologistica.intravio.dto.RomaneioDTO;
import com.intraviologistica.intravio.model.Romaneio;
import com.intraviologistica.intravio.service.RomaneioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/romaneios")
public class RomaneioController {

    private final RomaneioService romaneioService;

    public RomaneioController(RomaneioService romaneioService) {
        this.romaneioService = romaneioService;
    }

    @GetMapping
    public ResponseEntity<List<RomaneioDTO>> listar() {
        return ResponseEntity.ok(romaneioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RomaneioDTO> buscaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(romaneioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Romaneio> salvar(@Valid @RequestBody RomaneioDTO dto) {
        Romaneio romaneioSalvo = romaneioService.salvar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(romaneioSalvo.getId()).toUri();
        return ResponseEntity.created(location).body(romaneioSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Romaneio> atualizarRomaneio(@PathVariable Long id, @Valid @RequestBody RomaneioDTO romaneioDTO){
        romaneioDTO.setId(id);
        return ResponseEntity.ok(romaneioService.AtualizarRomaneio(romaneioDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluiRomaneio(@PathVariable Long id){
        romaneioService.excluiRomaneio(id);
        return ResponseEntity.noContent().build();
    }
}
