package com.intraviologistica.intravio.controller;

import com.intraviologistica.intravio.dto.TransportadorDTO;
import com.intraviologistica.intravio.model.Transportador;
import com.intraviologistica.intravio.service.TransportadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transportadores")
public class TransportadorController {

    private final TransportadorService transportadorService;

    public TransportadorController(TransportadorService transportadorService) {
        this.transportadorService = transportadorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportadorDTO> buscaTransportadorPorId(@PathVariable Long id) {
        TransportadorDTO transportadorDTO = transportadorService.buscaTransportadorPorIdDTO(id);
        return ResponseEntity.ok(transportadorDTO);
    }

    @GetMapping
    public ResponseEntity<List<TransportadorDTO>> lista() {
        List<TransportadorDTO> transportadorDTOList = transportadorService.lista();
        return ResponseEntity.ok(transportadorDTOList);
    }

    @PostMapping
    public ResponseEntity<Transportador> salva(@RequestBody TransportadorDTO dto) {
        Transportador transportador = transportadorService.salva(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transportador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transportador> atualiza(@PathVariable Long id, @RequestBody TransportadorDTO dto) {
        dto.setId(id);
        Transportador transportador = transportadorService.atualiza(dto);
        return ResponseEntity.ok(transportador);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleta(@PathVariable Long id) {
        transportadorService.deleta(id);
        return ResponseEntity.noContent().build();
    }

}
