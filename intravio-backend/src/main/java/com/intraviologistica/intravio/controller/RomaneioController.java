package com.intraviologistica.intravio.controller;

import com.intraviologistica.intravio.dto.input.RomaneioInputDTO;
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
    public ResponseEntity<List<RomaneioDTO>> listaRomaneios() {
        return ResponseEntity.ok(romaneioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RomaneioDTO> buscaRomaneioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(romaneioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<RomaneioDTO> salvaRomaneio(@Valid @RequestBody RomaneioInputDTO dto) {
        RomaneioDTO romaneioSalvo = romaneioService.salvar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(romaneioSalvo.getId()).toUri();
        return ResponseEntity.created(location).body(romaneioSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Romaneio> atualizarRomaneio(@PathVariable Long id, @Valid @RequestBody RomaneioInputDTO romaneioInputDTO) {
        romaneioInputDTO.setId(id);
        return ResponseEntity.ok(romaneioService.AtualizarRomaneio(romaneioInputDTO));
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<Void> alterarStatusDeTodosPedidosDoRomaneio(@PathVariable Long id, @PathVariable Integer status) {
        romaneioService.alterarStatusDeTodosPedidosParaEmTransito(id, status);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluiRomaneio(@PathVariable Long id) {
        romaneioService.excluiRomaneio(id);
        return ResponseEntity.noContent().build();
    }
}
