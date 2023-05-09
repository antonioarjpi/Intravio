package com.intraviologistica.intravio.controller;

import com.intraviologistica.intravio.dto.input.RomaneioFechamentoDTO;
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
@RequestMapping("/api/v1/romaneios")
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
    public ResponseEntity<RomaneioInputDTO> buscaRomaneioPorId(@PathVariable String id) {
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
    public ResponseEntity<Romaneio> atualizarRomaneio(@PathVariable String id, @Valid @RequestBody RomaneioInputDTO romaneioInputDTO) {
        romaneioInputDTO.setId(id);
        return ResponseEntity.ok(romaneioService.AtualizarRomaneio(romaneioInputDTO));
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<Void> alterarStatusDeTodosPedidosDoRomaneio(@PathVariable String id, @PathVariable Integer status) {
        romaneioService.alterarStatusDeTodosPedidosParaEmTransito(id, status);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/processar")
    public ResponseEntity<Void> processarRomaneio(@PathVariable String id) {
        //try{
            romaneioService.processarRomaneio(id);
            return ResponseEntity.noContent().build();
//        }catch (Exception e){
//            return ResponseEntity.internalServerError().build();
//        }
    }

    @PutMapping("/{id}/fechamento")
    public ResponseEntity<Void> fecharRomaneio(@PathVariable String id, @RequestBody RomaneioFechamentoDTO dto) {
        try{
            dto.setRomaneioId(id);
            romaneioService.fecharRomaneio(dto);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluiRomaneio(@PathVariable String id) {
        romaneioService.excluiRomaneio(id);
        return ResponseEntity.noContent().build();
    }
}
