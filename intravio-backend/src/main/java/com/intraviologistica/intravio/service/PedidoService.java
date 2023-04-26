package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.PedidoDTO;
import com.intraviologistica.intravio.model.*;
import com.intraviologistica.intravio.model.enums.StatusPedido;
import com.intraviologistica.intravio.repository.ItemRepository;
import com.intraviologistica.intravio.repository.PedidoRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final FilialService filialService;
    private final FuncionarioService funcionarioService;
    private final ItemRepository itemRepository;
    private final ProdutoService produtoService;

    public PedidoService(PedidoRepository pedidoRepository, FilialService filialService, FuncionarioService funcionarioService, ItemRepository itemRepository, ProdutoService produtoService) {
        this.pedidoRepository = pedidoRepository;
        this.filialService = filialService;
        this.funcionarioService = funcionarioService;
        this.itemRepository = itemRepository;
        this.produtoService = produtoService;
    }

    public Pedido findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + id));
    }

    @Transactional
    public List<PedidoDTO> listar() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoDTO buscarPorId(Long id) {
        return toDTO(findById(id));
    }

    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Pedido pedido = toEntity(dto);

        setPedido(dto, pedido);

        pedido.setStatus(StatusPedido.PENDENTE);

        pedido = pedidoRepository.save(pedido);

        for (Item item : pedido.getItens()) {
            Produto produto = produtoService.buscarProdutoPorId(item.getProduto().getId());
            item.setProduto(produto);
            item.setPreco(item.getProduto().getPreco());
            item.setPeso(item.getProduto().getPeso());
            item.setPedido(pedido);
        }

        itemRepository.saveAll(pedido.getItens());

        return pedido;
    }

    @Transactional
    public Pedido atualizar(PedidoDTO dto) {
        Pedido pedido = findById(dto.getId());

        setPedido(dto, pedido);

        pedido.setItens(dto.getItens());
        pedido.setFotos(dto.getFotos());
        pedido.setStatus(dto.getStatus());
        pedido.setPrioridade(dto.getPrioridade());

        for (Item item : pedido.getItens()) {
            Produto produto = produtoService.buscarProdutoPorId(item.getProduto().getId());
            item.setProduto(produto);
            item.setPreco(item.getProduto().getPreco());
            item.setPeso(item.getProduto().getPeso());
            item.setPedido(pedido);
        }

        itemRepository.saveAll(pedido.getItens());

        return pedido;
    }

    @Transactional
    public void excluir(Long id) {
        pedidoRepository.deleteById(id);
    }

    private void setPedido(PedidoDTO dto, Pedido pedido) {
        Filial origem = filialService.buscarPorNome(dto.getOrigem());
        Filial destino = filialService.buscarPorNome(dto.getDestino());
        Funcionario remetente = funcionarioService.buscarPorEmail(dto.getRemetente());
        Funcionario destinatario = funcionarioService.buscarPorEmail(dto.getDestinatario());

        pedido.setOrigem(origem);
        pedido.setDestino(destino);
        pedido.setRemetente(remetente);
        pedido.setDestinatario(destinatario);
    }

    public PedidoDTO toDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setItens(pedido.getItens());
        dto.setFotos(pedido.getFotos());
        dto.setStatus(pedido.getStatus());
        dto.setDataAtualizacao(pedido.getDataAtualizacao() == null ? pedido.getDataPedido() : pedido.getDataAtualizacao());
        dto.setDataPedido(pedido.getDataPedido());
        dto.setPrioridade(pedido.getPrioridade());
        dto.setAcompanhaStatus(pedido.getAcompanhaStatus());

        dto.setOrigem(pedido.getOrigem().getNome());
        dto.setDestino(pedido.getDestino().getNome());
        dto.setRemetente(pedido.getRemetente().getNome());
        dto.setDestinatario(pedido.getDestinatario().getNome());

        return dto;
    }

    public Pedido toEntity(PedidoDTO dto) {
        Pedido pedido = new Pedido();

        pedido.setId(dto.getId());
        pedido.setItens(dto.getItens());
        pedido.setFotos(dto.getFotos());
        pedido.setStatus(dto.getStatus());
        pedido.setDataAtualizacao(LocalDateTime.now());
        pedido.setPrioridade(dto.getPrioridade());
        pedido.setAcompanhaStatus(dto.getAcompanhaStatus());

        return pedido;
    }
}
