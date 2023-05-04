package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.HistoricoPedidoDTO;
import com.intraviologistica.intravio.dto.PedidoDTO;
import com.intraviologistica.intravio.dto.input.PedidoInputDTO;
import com.intraviologistica.intravio.model.*;
import com.intraviologistica.intravio.model.enums.StatusPedido;
import com.intraviologistica.intravio.repository.PedidoRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import com.intraviologistica.intravio.service.exceptions.RuleOfBusinessException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final FilialService filialService;
    private final FuncionarioService funcionarioService;
    private final ItemService itemService;
    private final FileService fileService;
    private final ProdutoService produtoService;

    private static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public PedidoService(PedidoRepository pedidoRepository, FilialService filialService, FuncionarioService funcionarioService, ItemService itemService, FileService fileService, ProdutoService produtoService) {
        this.pedidoRepository = pedidoRepository;
        this.filialService = filialService;
        this.funcionarioService = funcionarioService;
        this.itemService = itemService;
        this.fileService = fileService;
        this.produtoService = produtoService;
    }

    @Transactional
    public Pedido findById(String id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + id));
    }

    @Transactional
    public Pedido buscaPorNumeroPedido(Integer numeroPedido) {
        return pedidoRepository.findByNumeroPedido(numeroPedido)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + numeroPedido));
    }

    @Transactional
    public List<PedidoDTO> listaPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Pedido::getNumeroPedido).reversed())
                .map(x -> new PedidoDTO(x))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Pedido> listaPedidosPorRomaneioId(String id) {
        List<Pedido> pedidos = pedidoRepository.findByRomaneioId(id);
        return pedidos;
    }

    @Transactional
    public List<HistoricoPedidoDTO> listaHistoricoDoPedido(String codigo) {
        List<HistoricoPedido> historicoPedidos = pedidoRepository.findByCodigoRastreio(codigo);

        if (historicoPedidos.isEmpty()) {
            throw new ResourceNotFoundException("Código de rastreio não localizado");
        }

        return historicoPedidos
                .stream()
                .map(x -> new HistoricoPedidoDTO(x))
                .collect(Collectors.toList());
    }

    // Busca por ID e Retorna a entidade convertida para DTO
    @Transactional
    public PedidoDTO buscarPedidosPorId(String id) {
        Pedido pedido = findById(id);
        return new PedidoDTO(pedido);
    }

    // Salva pedido retornando a entidade, sem validações
    @Transactional
    public Pedido salvaPedidoRetornandoEntidade(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido salvarPedido(PedidoInputDTO dto) {
        dto.setId(getUuid());
        Pedido pedido = toEntity(dto);

        Integer maxNumeroPedido = pedidoRepository.getMaxNumeroPedido(); //Busca o maior numero de pedido
        pedido.setNumeroPedido(maxNumeroPedido + 1); // Atribui no numero de Pedido o maior numero encontrado + 1

        atribuiDadosDtoAoPedido(dto, pedido);

        pedido.setCodigoRastreio(geraCodigoRastreio(pedido));  // Atribui o código de rastreio ao pedido

        pedido.atualizarStatus(StatusPedido.PENDENTE, "Pedido recebido pelo setor de suprimentos");

        pedido = pedidoRepository.save(pedido);

        adicionaItensNoPedido(pedido);

        return pedido;
    }

    @Transactional
    public Pedido atualizaPedido(PedidoInputDTO dto) {
        Pedido pedido = findById(dto.getId());

        pedidoRepository.deleteItemPedidoByPedidoId(pedido.getId()); // Remove os itens desse pedido antes de adicionar os itens que veio no parametro.

        atribuiDadosDtoAoPedido(dto, pedido);

        attPedido(dto, pedido);

        pedido = pedidoRepository.save(pedido);

        adicionaItensNoPedido(pedido);

        return pedido;
    }

    @Transactional
    public void adicionaImagensPedido(String id, MultipartFile[] files) throws Exception {
        Pedido pedido = findById(id);
        String path = "src/main/resources/static/pedidos/enviadas/";

        fileService.excluiFotoDoPedido(files, pedido.getImagens(), path);
        salvaFotoNoPedido(files, pedido, path);
        salvaPedidoRetornandoEntidade(pedido);
    }

    // Armazena a foto no sistema e atribui ao pedido
    private void salvaFotoNoPedido(MultipartFile[] files, Pedido pedido, String path) throws Exception {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String filename = fileService.enviaArquivo(files[i], path);
            list.add(filename);
        }
        pedido.setImagens(list);
    }

    //Exibe imagem do pedido
    public void getImagens(String nomeArquivo, HttpServletResponse response) throws IOException {
        fileService.baixarArquivo(nomeArquivo, "src/main/resources/static/pedidos/enviadas/", response);
    }

    // Altera Status do Pedido para Cancelado.
    @Transactional
    public void cancelaPedido(String id, String motivo) {
        Pedido pedido = findById(id);
        if (pedido.getStatus().ordinal() == 1) {
            throw new RuleOfBusinessException("Erro: Pedido já foi cancelado e não pode ser cancelado novamente");
        } else if (pedido.getStatus().ordinal() != 0) {
            throw new RuleOfBusinessException("Erro: Somente pedidos com status 'PENDENTE' pode ser cancelado. Por favor, corrija e tente novamente");
        } else {
            pedido.atualizarStatus(StatusPedido.CANCELADO, motivo);
        }
    }

    // Busca e atribui no pedido: origem, destino, remetente e destinatario.
    private void atribuiDadosDtoAoPedido(PedidoInputDTO dto, Pedido pedido) {
        Filial origem = filialService.findById(dto.getOrigem());
        Filial destino = filialService.findById(dto.getDestino());
        Funcionario remetente = funcionarioService.findById(dto.getRemetente());
        Funcionario destinatario = funcionarioService.findById(dto.getDestinatario());

        pedido.setOrigem(origem);
        pedido.setDestino(destino);
        pedido.setRemetente(remetente);
        pedido.setDestinatario(destinatario);
    }

    private void adicionaItensNoPedido(Pedido pedido) {
        for (Item item : pedido.getItens()) {
            Produto produto = produtoService.buscaProdutoPorId(item.getProduto().getId());
            item.setProduto(produto);
            item.setPreco(item.getProduto().getPreco());
            item.setPeso(item.getProduto().getPeso());
            item.setPedido(pedido);
        }

        itemService.salvarListaDeItens(pedido.getItens());
    }

    // Atribui dados DTO na entidade Pedido
    private static void attPedido(PedidoInputDTO dto, Pedido pedido) {
        pedido.setItens(dto.getItens());
        pedido.setImagens(dto.getFotos());
        pedido.setPrioridade(dto.getPrioridade());
        pedido.setAcompanhaStatus(dto.getAcompanhaStatus());
    }

    private String geraCodigoRastreio(Pedido pedido) {
        // Extrai as duas primeiras letras do nome da filial de origem
        String origem = pedido.getOrigem().getNome().substring(0, 2);

        // Extrai as três primeiras letras do nome da filial de destino
        String destino = pedido.getDestino().getNome().substring(0, 3);

        // Gera uma sequência de 8 números aleatórios
        String numero = "";

        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            int i1 = random.nextInt(9);
            numero = numero + i1;
        }

        // Concatena as partes para formar o código de rastreio
        String codigoRastreio = "Y" + origem + numero + destino;

        return codigoRastreio.toUpperCase(Locale.ROOT);
    }

    // Converte DTO para Entidade
    public Pedido toEntity(PedidoInputDTO dto) {
        Pedido pedido = new Pedido();

        pedido.setItens(dto.getItens());
        pedido.setId(dto.getId());
        pedido.setImagens(dto.getFotos());
        pedido.setDataAtualizacao(LocalDateTime.now());
        pedido.setPrioridade(dto.getPrioridade());
        pedido.setAcompanhaStatus(dto.getAcompanhaStatus());

        return pedido;
    }
}