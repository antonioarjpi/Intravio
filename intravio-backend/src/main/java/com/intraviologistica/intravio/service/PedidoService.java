package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.HistoricoPedidoDTO;
import com.intraviologistica.intravio.dto.MotivoCancelamentoDTO;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    private final EmailService emailService;

    private static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public PedidoService(PedidoRepository pedidoRepository, FilialService filialService, FuncionarioService funcionarioService, ItemService itemService, FileService fileService, ProdutoService produtoService, EmailService emailService) {
        this.pedidoRepository = pedidoRepository;
        this.filialService = filialService;
        this.funcionarioService = funcionarioService;
        this.itemService = itemService;
        this.fileService = fileService;
        this.produtoService = produtoService;
        this.emailService = emailService;
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
    public List<PedidoDTO> listaPedidos(String minDate, String maxDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime hoje = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        LocalDateTime min = minDate.equals("") ? hoje.minusDays(1) : LocalDateTime.parse(minDate + "T00:00:00", formatter);
        LocalDateTime max = maxDate.equals("") ? hoje : LocalDateTime.parse(maxDate + "T23:59:59", formatter);

        return pedidoRepository.findAll(min, max)
                .stream()
                .map(x -> new PedidoDTO(x))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Pedido> listaPedidosPorRomaneioId(String id) {
        List<Pedido> pedidos = pedidoRepository.findByRomaneioId(id);
        return pedidos;
    }

    @Transactional
    public List<PedidoDTO> listaPedidosPorStatus(Integer status) {
        List<Pedido> pedidos = pedidoRepository.findAllByStatus(status);
        return pedidos.stream()
                .map(x -> new PedidoDTO(x))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<HistoricoPedidoDTO> listaHistoricoDoPedido(String codigo) {
        List<HistoricoPedido> historicoPedidos = pedidoRepository.findByCodigoRastreio(codigo);

        if (historicoPedidos.isEmpty()) {
            throw new ResourceNotFoundException("Código de rastreio não localizado");
        }

        return historicoPedidos
                .stream()
                .sorted(Comparator.comparing(HistoricoPedido::getDataAtualizacao).reversed())
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

        final Pedido pedidoFinal = pedido;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                envioDeEmail(pedidoFinal);
            }
        }, 10000);

        return pedido;
    }

    public void envioDeEmail(Pedido pedido) {
        if (pedido.getAcompanhaStatus().ordinal() == 1) {
            envioEmailDestinatarioPedidoPendente(pedido);
        } else if (pedido.getAcompanhaStatus().ordinal() == 2) {
            envioEmailRemetentePedidoPendente(pedido);
        } else if (pedido.getAcompanhaStatus().ordinal() == 3) {
            envioEmailRemetentePedidoPendente(pedido);
            envioEmailDestinatarioPedidoPendente(pedido);
        }
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
    public void salvaFotoNoPedido(MultipartFile[] files, Pedido pedido, String path) throws Exception {
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

    //Exibe imagem do pedido
    public void baixarImagensPedido(String id, HttpServletResponse response) throws IOException {
        List<String> imagens = findById(id).getImagens();
        fileService.baixarArquivos(imagens, "src/main/resources/static/pedidos/enviadas/", response);
    }

    // Altera Status do Pedido para Cancelado.
    @Transactional
    public void cancelaPedido(String id, MotivoCancelamentoDTO motivo) {
        Pedido pedido = findById(id);
        if (pedido.getStatus().ordinal() == 1) {
            throw new RuleOfBusinessException("Erro: Pedido já foi cancelado e não pode ser cancelado novamente");
        } else if (pedido.getStatus().ordinal() != 0) {
            throw new RuleOfBusinessException("Erro: Somente pedidos com status 'PENDENTE' pode ser cancelado. Por favor, corrija e tente novamente");
        } else {
            pedido.atualizarStatus(StatusPedido.CANCELADO, motivo.getMotivo());
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
        pedido.setDataAtualizacao(LocalDateTime.now());
    }

    private String geraCodigoRastreio(Pedido pedido) {
        // Extrai as duas primeiras letras do nome da filial de origem
        String origem = pedido.getOrigem().getEndereco().getCidade().substring(0, 1);

        // Extrai as três primeiras letras do nome da filial de destino
        String destino = pedido.getDestino().getEndereco().getCidade().substring(0, 1);

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

    public void envioEmailRecebimentoPedido(Pedido pedido, String nome) {
        String assunto = "Pedido Entregue";
        StringBuilder conteudo = new StringBuilder();
        conteudo.append("<p>Prezado(a) ").append(nome).append(",</p>");
        conteudo.append("<p>Gostaríamos de informar que o seu pedido <strong>").append(pedido.getNumeroPedido()).append("</strong> foi entregue com sucesso.</p>");
        conteudo.append("<p>Agradecemos por utilizar nossos serviços.</p>");
        conteudo.append("<p>Atenciosamente,</p>");
        conteudo.append("<p>Intravio</p>");

        emailService.enviarEmail(pedido.getRemetente().getEmail(), assunto, conteudo.toString());
    }

    public void envioEmailRemetentePedidoPendente(Pedido pedido) {
        String[] nomes = pedido.getDestinatario().getNome().split(" ");
        String primeiroNome = nomes[0];

        String assunto = "Entrega de Objeto - Código Rastreio";

        StringBuilder conteudo = new StringBuilder();
        conteudo.append("<p>Prezado(a) ").append(primeiroNome).append(",</p>");
        conteudo.append("<p>Gostaríamos de informar que o seu objeto foi recebido na unidade ").append(pedido.getOrigem().getNome()).append(" com sucesso e logo será enviado. A seguir, fornecemos o código de rastreio para que você possa acompanhar o status do seu envio:</p>");
        conteudo.append("<p><strong>Número do Pedido:</strong> ").append(pedido.getNumeroPedido()).append("</p>");
        conteudo.append("<p><strong>Código de Rastreio:</strong> ").append(pedido.getCodigoRastreio()).append("</p>");
        tabelaDeItensDoPedidoHtml(pedido, conteudo);
        conteudo.append("<p>Utilize este código para rastrear a localização e o status do seu objeto através do nosso site.</p>");
        conteudo.append("<p>Agradecemos a sua confiança em nossos serviços e estamos à disposição para qualquer dúvida ou assistência adicional.</p>");
        conteudo.append("<p>Atenciosamente,</p>");
        conteudo.append("<p>Intravio</p>");

        emailService.enviarEmail(pedido.getRemetente().getEmail(), assunto, conteudo.toString());
    }

    public void envioEmailDestinatarioPedidoPendente(Pedido pedido) {
        String[] nomes = pedido.getDestinatario().getNome().split(" ");
        String primeiroNome = nomes[0];

        String assunto = "Entrega de Objeto - Código Rastreio";

        StringBuilder conteudo = new StringBuilder();
        conteudo.append("<p>Prezado(a) ").append(primeiroNome).append("</p>");
        conteudo.append("<p>Gostaríamos de informar que um objeto foi postado para ser entregue em seu nome. Seguem os detalhes referentes ao envio:</p>");
        conteudo.append("<p><strong>Código de Rastreio:</strong> ").append(pedido.getCodigoRastreio()).append("<br/>");
        conteudo.append("<strong>Número do Pedido:</strong> ").append(pedido.getNumeroPedido()).append("<br/>");
        conteudo.append("<strong>Remetente: </strong>").append(pedido.getRemetente().getNome()).append("<br/>");
        conteudo.append("<strong>Data de Envio: </strong>").append(pedido.getDataPedido()).append("</p>");
        tabelaDeItensDoPedidoHtml(pedido, conteudo);
        conteudo.append("<p>Pedimos desculpas se esta entrega veio como uma surpresa. Caso tenha alguma dúvida ou se precisar devolver o objeto, entre em contato conosco o mais breve possível para que possamos resolver a situação adequadamente.</p>");
        conteudo.append("<p>Estamos à disposição para ajudar no que for necessário.</p>");
        conteudo.append("<p>Atenciosamente,</p>");
        conteudo.append("<p>Intravio</p>");

        emailService.enviarEmail(pedido.getRemetente().getEmail(), assunto, conteudo.toString());
    }

    public static void tabelaDeItensDoPedidoHtml(Pedido pedido, StringBuilder conteudo) {
        conteudo.append("<table border=\"1\">");
        conteudo.append("<tr><th>Produto</th><th>Quantidade</th><th>Descrição</th></tr>");
        for (Item item : pedido.getItens()) {
            conteudo.append("<tr>");
            conteudo.append("<td>").append(item.getProduto().getNome()).append("</td>");
            conteudo.append("<td>").append(item.getQuantidade()).append("</td>");
            conteudo.append("<td>").append(item.getDescricao()).append("</td>");
            conteudo.append("</tr>");
        }
        conteudo.append("</table>");
    }
}