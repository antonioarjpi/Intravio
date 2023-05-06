package com.intraviologistica.intravio.controller;

import com.intraviologistica.intravio.dto.HistoricoPedidoDTO;
import com.intraviologistica.intravio.dto.MotivoCancelamentoDTO;
import com.intraviologistica.intravio.dto.input.PedidoInputDTO;
import com.intraviologistica.intravio.dto.PedidoDTO;
import com.intraviologistica.intravio.model.Pedido;
import com.intraviologistica.intravio.service.PedidoService;
import com.intraviologistica.intravio.service.exceptions.FileNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarPedidos() {
        List<PedidoDTO> pedidosDTO = pedidoService.listaPedidos();
        return ResponseEntity.ok(pedidosDTO);
    }

    @GetMapping("/status")
    public ResponseEntity<List<PedidoDTO>> listarPedidosPorStatus(@RequestParam(defaultValue = "PENDENTE") Integer status) {
        List<PedidoDTO> pedidosDTO = pedidoService.listaPedidosPorStatus(status);
        return ResponseEntity.ok(pedidosDTO);
    }

    @GetMapping("/busca/completa/{id}")
    public ResponseEntity<PedidoDTO> buscarPedidoPorId(@PathVariable String id) {
        PedidoDTO pedidoDTO = pedidoService.buscarPedidosPorId(id);
        return ResponseEntity.ok(pedidoDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoInputDTO> buscarPedidoPorIdRetornaDTO(@PathVariable String id) {
        Pedido pedido = pedidoService.findById(id);
        return ResponseEntity.ok(new PedidoInputDTO(pedido));
    }

    @GetMapping("/rastreio/{codigo}")
    public ResponseEntity<List<HistoricoPedidoDTO>> listaHistoricoDoPedido(@PathVariable String codigo) {
        return ResponseEntity.ok(pedidoService.listaHistoricoDoPedido(codigo));
    }

    @PostMapping
    public ResponseEntity<Pedido> salvarPedido(@RequestBody PedidoInputDTO pedidoInputDTO) {
        Pedido pedidoSalvo = pedidoService.salvarPedido(pedidoInputDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(pedidoSalvo.getId()).toUri();
        return ResponseEntity.created(location).body(pedidoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizarPedido(@PathVariable String id, @RequestBody PedidoInputDTO pedidoInputDTO) {
        pedidoInputDTO.setId(id);
        Pedido pedidoAtualizado = pedidoService.atualizaPedido(pedidoInputDTO);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @PostMapping("/{id}/imagem/adicionar")
    public ResponseEntity<Void> adicionaImagemPedido(@PathVariable String id, MultipartFile[] files) throws Exception {
        pedidoService.adicionaImagensPedido(id, files);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{filename}/imagens")
    public ResponseEntity<Void> exibeImagensPedido(@PathVariable String filename, HttpServletResponse response) throws IOException {
        try {
            pedidoService.getImagens(filename, response);
            return ResponseEntity.noContent().build();
        } catch (FileNotFoundException ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/download/imagens")
    public ResponseEntity<Void> baixarImagens(@PathVariable String id, HttpServletResponse response) throws IOException {
        try {
            pedidoService.baixarImagensPedido(id, response);
            return ResponseEntity.noContent().build();
        } catch (FileNotFoundException ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable String id, @RequestBody MotivoCancelamentoDTO motivo) {
        pedidoService.cancelaPedido(id, motivo);
        return ResponseEntity.noContent().build();
    }
}
