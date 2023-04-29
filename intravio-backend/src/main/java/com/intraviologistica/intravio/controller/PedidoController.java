package com.intraviologistica.intravio.controller;

import com.intraviologistica.intravio.dto.HistoricoPedidoDTO;
import com.intraviologistica.intravio.dto.input.PedidoInputDTO;
import com.intraviologistica.intravio.dto.PedidoDTO;
import com.intraviologistica.intravio.model.Pedido;
import com.intraviologistica.intravio.service.PedidoService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> buscarPedidoPorId(@PathVariable String id) {
        PedidoDTO pedidoDTO = pedidoService.buscarPedidosPorId(id);
        return ResponseEntity.ok(pedidoDTO);
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

    @GetMapping("/{id}/imagem/exibe")
    public ResponseEntity<Resource> exibeImagemPedido(@PathVariable String id) throws Exception {
       Resource imagem = pedidoService.exibeImagensPedido(id);

        if (imagem.exists()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);
            return new ResponseEntity<>(imagem, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPedido(@PathVariable String id, @RequestBody String motivo) {
        pedidoService.cancelaPedido(id, motivo);
        return ResponseEntity.noContent().build();
    }
}
