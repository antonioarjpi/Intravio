package com.intraviologistica.intravio.controller;

import com.intraviologistica.intravio.dto.TransportadorDTO;
import com.intraviologistica.intravio.model.Transportador;
import com.intraviologistica.intravio.service.TransportadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transportadores")
public class TransportadorController {

    private final TransportadorService transportadorService;

    public TransportadorController(TransportadorService transportadorService) {
        this.transportadorService = transportadorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportadorDTO> buscaTransportadorPorId(@PathVariable String id) {
        TransportadorDTO transportadorDTO = transportadorService.buscaTransportadorPorId(id);
        return ResponseEntity.ok(transportadorDTO);
    }

    @GetMapping
    public ResponseEntity<List<TransportadorDTO>> listaTransportadores() {
        List<TransportadorDTO> transportadorDTOList = transportadorService.listaTodosTransportadores();
        return ResponseEntity.ok(transportadorDTOList);
    }

    @PostMapping
    public ResponseEntity<Transportador> salvaTransportador(@RequestBody TransportadorDTO dto) {
        Transportador transportador = transportadorService.salvaTransportador(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transportador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transportador> atualizaTransportador(@PathVariable String id, @RequestBody TransportadorDTO dto) {
        dto.setId(id);
        Transportador transportador = transportadorService.atualizaTransportador(dto);
        return ResponseEntity.ok(transportador);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletaTransportador(@PathVariable String id) {
        transportadorService.deletaTransportador(id);
        return ResponseEntity.noContent().build();
    }

}
