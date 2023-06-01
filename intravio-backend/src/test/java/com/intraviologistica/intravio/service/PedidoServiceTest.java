package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.HistoricoPedidoDTO;
import com.intraviologistica.intravio.dto.MotivoCancelamentoDTO;
import com.intraviologistica.intravio.dto.PedidoDTO;
import com.intraviologistica.intravio.dto.input.PedidoInputDTO;
import com.intraviologistica.intravio.model.*;
import com.intraviologistica.intravio.model.enums.AcompanhaStatus;
import com.intraviologistica.intravio.model.enums.Prioridade;
import com.intraviologistica.intravio.model.enums.StatusPedido;
import com.intraviologistica.intravio.repository.PedidoRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import com.intraviologistica.intravio.service.exceptions.RuleOfBusinessException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private FilialService filialService;

    @Mock
    private FuncionarioService funcionarioService;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private ItemService itemService;

    @Mock
    private FileService fileService;

    @InjectMocks
    private PedidoService pedidoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById_PedidoExistente() {
        String id = "1";

        Pedido pedido = getPedido();

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido)); // Simule o comportamento do método findById() do repositório para retornar o pedido simulado

        Pedido result = pedidoService.findById(id); // Chame o método do serviço para buscar o pedido pelo ID

        verify(pedidoRepository, times(1)).findById(id); // Verifique se o método findById() do repositório foi chamado uma vez com o ID correto

        assertEquals(pedido, result); // Verifique se o resultado retornado é o pedido simulado
    }

    @Test
    public void testFindById_PedidoNaoEncontrado() {
        String id = "1";

        when(pedidoRepository.findById(id)).thenReturn(Optional.empty()); // Simule o comportamento do método findById() do repositório para retornar um Optional vazio

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> pedidoService.findById(id));  // Chame o método do serviço para buscar o pedido pelo ID e verifique se uma exceção é lançada

        verify(pedidoRepository, times(1)).findById(id); // Verifique se o método findById() do repositório foi chamado uma vez com o ID correto
    }

    @Test
    public void testBuscaPorNumeroPedido_PedidoExistente() {
        Integer numeroPedido = 1234;
        Pedido pedidoSimulado = getPedido();

        when(pedidoRepository.findByNumeroPedido(numeroPedido)).thenReturn(Optional.of(pedidoSimulado)); // Simule o comportamento do método findByNumeroPedido() do repositório para retornar o pedido simulado

        Pedido result = pedidoService.buscaPorNumeroPedido(numeroPedido); // Chame o método do serviço para buscar o pedido por número do pedido

        verify(pedidoRepository, times(1)).findByNumeroPedido(numeroPedido);  // Verifique se o método findByNumeroPedido() do repositório foi chamado uma vez com o número do pedido correto

        assertEquals(pedidoSimulado, result); // Verifique se o resultado retornado é o pedido simulado
    }

    @Test
    public void testBuscaPorNumeroPedido_PedidoNaoExistente() {
        Integer numeroPedido = 5678;

        when(pedidoRepository.findByNumeroPedido(numeroPedido)).thenReturn(Optional.empty()); // Simule o comportamento do método findByNumeroPedido() do repositório para retornar um Optional vazio

        Assertions.assertThrows(ResourceNotFoundException.class, () -> pedidoService.buscaPorNumeroPedido(numeroPedido)); // Chame o método do serviço para buscar o pedido por número do pedido

        verify(pedidoRepository, times(1)).findByNumeroPedido(numeroPedido); // Verifique se o método findByNumeroPedido() do repositório foi chamado uma vez com o número do pedido correto
    }

    @Test
    public void testListaPedidos() {
        String minDate = "2023-01-01";
        String maxDate = "2023-12-31";

        List<Pedido> pedidosSimulados = new ArrayList<>();
        pedidosSimulados.add(getPedido());
        pedidosSimulados.add(getPedido());
        pedidosSimulados.add(getPedido());

        when(pedidoRepository.findAll(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(pedidosSimulados);         // Simule o comportamento do método findAll() do repositório para retornar a lista simulada de pedidos

        List<PedidoDTO> result = pedidoService.listaPedidos(minDate, maxDate);  // Chame o método do serviço para listar os pedidos

        verify(pedidoRepository, times(1)).findAll(any(LocalDateTime.class), any(LocalDateTime.class)); // Verifique se o método findAll() do repositório foi chamado uma vez com as datas mínima e máxima corretas

        assertEquals(pedidosSimulados.size(), result.size());// Verifique se o resultado retornado possui a mesma quantidade de pedidos da lista simulada
    }

    @Test
    public void testListaPedidosPorStatus() {
        Integer status = 1;

        List<Pedido> pedidosSimulados = new ArrayList<>();
        pedidosSimulados.add(getPedido());
        pedidosSimulados.add(getPedido());

        when(pedidoRepository.findAllByStatus(status)).thenReturn(pedidosSimulados); // Simule o comportamento do método findAllByStatus() do repositório para retornar a lista simulada de pedidos

        List<PedidoDTO> result = pedidoService.listaPedidosPorStatus(status); // Chame o método do serviço para listar os pedidos por status

        verify(pedidoRepository, times(1)).findAllByStatus(status); // Verifique se o método findAllByStatus() do repositório foi chamado uma vez com o status correto

        assertEquals(pedidosSimulados.size(), result.size());  // Verifique se o resultado retornado possui a mesma quantidade de pedidos da lista simulada
    }

    @Test
    public void testListaHistoricoDoPedido() {
        String codigoRastreio = "ABC123";

        HistoricoPedido historicoPedido = new HistoricoPedido();
        historicoPedido.setStatusAnterior(null);
        historicoPedido.setStatusAtual(StatusPedido.PENDENTE);
        historicoPedido.setDataAtualizacao(LocalDateTime.now());
        historicoPedido.setComentario("Pedido cadastrado");
        historicoPedido.setPedido(getPedido());

        List<HistoricoPedido> historicoPedidosSimulados = new ArrayList<>();
        historicoPedidosSimulados.add(historicoPedido);
        historicoPedidosSimulados.add(historicoPedido);

        when(pedidoRepository.findByCodigoRastreio(codigoRastreio)).thenReturn(historicoPedidosSimulados); // Simule o comportamento do método findByCodigoRastreio() do repositório para retornar a lista simulada de histórico de pedidos

        List<HistoricoPedidoDTO> result = pedidoService.listaHistoricoDoPedido(codigoRastreio); // Chame o método do serviço para obter o histórico do pedido

        verify(pedidoRepository, times(1)).findByCodigoRastreio(codigoRastreio); // Verifique se o método findByCodigoRastreio() do repositório foi chamado uma vez com o código de rastreio correto

        assertEquals(historicoPedidosSimulados.size(), result.size()); // Verifique se o resultado retornado possui a mesma quantidade de histórico de pedidos da lista simulada
    }

    @Test
    public void testListaHistoricoDoPedidoCodigoRastreioNaoLocalizado() {
        String codigoRastreio = "XYZ456";

        when(pedidoRepository.findByCodigoRastreio(codigoRastreio)).thenReturn(Collections.emptyList()); // Simule o comportamento do método findByCodigoRastreio() do repositório para retornar uma lista vazia

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            pedidoService.listaHistoricoDoPedido(codigoRastreio);
        }); // Teste se a exceção ResourceNotFoundException é lançada ao chamar o método do serviço

        verify(pedidoRepository, times(1)).findByCodigoRastreio(codigoRastreio); // Verifique se o método findByCodigoRastreio() do repositório foi chamado uma vez com o código de rastreio correto
    }

    @Test
    public void testBuscarPedidosPorId_PedidoExistente() {
        String id = "1";

        Pedido pedido = getPedido();

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido)); // Simule o comportamento do método findById() do repositório para retornar o pedido simulado

        PedidoDTO result = pedidoService.buscarPedidosPorId(id); // Chame o método do serviço para buscar o pedido pelo ID

        verify(pedidoRepository, times(1)).findById(id); // Verifique se o método findById() do repositório foi chamado uma vez com o ID correto

        assertEquals(pedido.getNumeroPedido(), result.getNumeroPedido());
        assertEquals(pedido.getId(), result.getId());
    }

    @Test
    public void testBuscarPedidosPorId_PedidoNaoEncontrado() {
        String id = "1";

        when(pedidoRepository.findById(id)).thenReturn(Optional.empty()); // Simule o comportamento do método findById() do repositório para retornar um Optional vazio

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> pedidoService.buscarPedidosPorId(id));  // Chame o método do serviço para buscar o pedido pelo ID e verifique se uma exceção é lançada

        verify(pedidoRepository, times(1)).findById(id); // Verifique se o método findById() do repositório foi chamado uma vez com o ID correto
    }

    @Test
    public void testSalvarPedido() {
        PedidoInputDTO pedidoInputDTO = new PedidoInputDTO();
        pedidoInputDTO.setItens(List.of(getItem()));
        pedidoInputDTO.setOrigem(1L);
        pedidoInputDTO.setDestino(2L);
        pedidoInputDTO.setRemetente("1");
        pedidoInputDTO.setDestinatario("2");
        pedidoInputDTO.setFotos(List.of("imagem1.png", "imagem2.png"));
        pedidoInputDTO.setAcompanhaStatus(AcompanhaStatus.NAO);
        pedidoInputDTO.setPrioridade(Prioridade.URGENTE);
        pedidoInputDTO.setDataPedido(LocalDateTime.now());
        pedidoInputDTO.setNumeroPedido(1);

        Filial origem = getFilialOrigem();
        Filial destino = getFilialDestino();
        Funcionario remetente = getFuncionarioRemetente();
        Funcionario destinatario = getFuncionarioDestinatario();
        Produto produto = getProduto();

        // Configurar o comportamento dos serviços auxiliares
        when(filialService.findById(pedidoInputDTO.getOrigem())).thenReturn(origem);
        when(filialService.findById(pedidoInputDTO.getDestino())).thenReturn(destino);
        when(funcionarioService.findById(pedidoInputDTO.getRemetente())).thenReturn(remetente);
        when(funcionarioService.findById(pedidoInputDTO.getDestinatario())).thenReturn(destinatario);
        when(produtoService.buscaProdutoPorId(any())).thenReturn(produto);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(getPedido());

        // Chamar o método do serviço
        pedidoService.salvarPedido(pedidoInputDTO);

        verify(pedidoRepository, times(1)).save(any(Pedido.class)); // Verificar se o pedido foi salvo corretamente no repositório

        // Verificar se os métodos auxiliares foram chamados adequadamente
        verify(filialService, times(1)).findById(pedidoInputDTO.getOrigem());
        verify(filialService, times(1)).findById(pedidoInputDTO.getDestino());
        verify(funcionarioService, times(1)).findById(pedidoInputDTO.getRemetente());
        verify(funcionarioService, times(1)).findById(pedidoInputDTO.getDestinatario());
        verify(produtoService, times(pedidoInputDTO.getItens().size())).buscaProdutoPorId(any());
        verify(itemService, times(pedidoInputDTO.getItens().size())).salvarListaDeItens(any());
    }

    @Test
    public void testSalvaPedidoRetornandoEntidade() {
        Pedido pedido = getPedido();

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido pedidoSalvo = pedidoService.salvaPedidoRetornandoEntidade(pedido);

        verify(pedidoRepository, times(1)).save(any(Pedido.class)); // Verificar se o pedido foi salvo corretamente no repositório

        assertEquals(pedido, pedidoSalvo); // Verificar se o retorno é igual à entidade salva
    }

    @Test
    public void testAtualizaPedido() {
        PedidoInputDTO dto = new PedidoInputDTO();
        dto.setItens(List.of(getItem()));
        dto.setOrigem(1L);
        dto.setDestino(2L);
        dto.setRemetente("1");
        dto.setDestinatario("2");
        dto.setFotos(List.of("imagem1.png", "imagem2.png"));
        dto.setAcompanhaStatus(AcompanhaStatus.NAO);
        dto.setPrioridade(Prioridade.URGENTE);

        Pedido pedidoExistente = getPedido();

        // Configurar os comportamentos dos mocks
        when(pedidoRepository.findById(dto.getId())).thenReturn(Optional.of(pedidoExistente));
        when(filialService.findById(dto.getOrigem())).thenReturn(new Filial());
        when(filialService.findById(dto.getDestino())).thenReturn(new Filial());
        when(funcionarioService.findById(dto.getRemetente())).thenReturn(new Funcionario());
        when(funcionarioService.findById(dto.getDestinatario())).thenReturn(new Funcionario());
        when(produtoService.buscaProdutoPorId(anyString())).thenReturn(new Produto());
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoExistente);

        Pedido result = pedidoService.atualizaPedido(dto);

        // Verificar se os métodos de busca foram chamados corretamente
        verify(pedidoRepository, times(1)).findById(dto.getId());

        verify(filialService, times(1)).findById(dto.getOrigem());

        verify(filialService, times(1)).findById(dto.getDestino());

        verify(funcionarioService, times(1)).findById(dto.getRemetente());

        verify(funcionarioService, times(1)).findById(dto.getDestinatario());

        verify(produtoService, times(dto.getItens().size())).buscaProdutoPorId(anyString());

        verify(pedidoRepository, times(1)).deleteItemPedidoByPedidoId(pedidoExistente.getId()); // Verificar se o método de exclusão de itens foi chamado corretamente

        verify(itemService, times(dto.getItens().size())).salvarListaDeItens(eq(pedidoExistente.getItens())); // Verificar se o método de salvamento de itens foi chamado corretamente

        verify(pedidoRepository, times(1)).save(pedidoExistente); // Verificar se o método de salvamento do pedido foi chamado corretamente

        assertEquals(pedidoExistente, result);  // Verificar o resultado
    }

    @Test
    public void testAdicionaImagensPedido() throws Exception {
        Pedido pedidoExistente = new Pedido();
        pedidoExistente.setId("1");

        MockMultipartFile[] imagens = new MockMultipartFile[2];
        imagens[0] = new MockMultipartFile("imagem1.jpg", "imagem1.jpg", "image/jpeg", new byte[0]);
        imagens[1] = new MockMultipartFile("imagem2.jpg", "imagem2.jpg", "image/jpeg", new byte[0]);

        when(pedidoRepository.findById("1")).thenReturn(Optional.of(pedidoExistente)); // Configurar os comportamentos dos mocks

        pedidoService.adicionaImagensPedido("1", imagens); // Chamar o método a ser testado

        verify(pedidoRepository, times(1)).findById("1"); // Verificar se o método de busca do pedido foi chamado corretamente

        verify(fileService, times(1)).excluiFotoDoPedido(eq(imagens), any(), anyString()); // Verificar se o método de exclusão de imagens foi chamado corretamente

        verify(fileService, times(1)).enviaArquivo(imagens[0], "src/main/resources/static/pedidos/enviadas/"); // Verificar se o método de salvamento de imagens foi chamado corretamente

        verify(pedidoRepository, times(1)).save(pedidoExistente); // Verificar se o método de salvamento do pedido foi chamado corretamente
    }

    @Test
    public void testGetImagens() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);

        pedidoService.getImagens("nomeArquivo.jpg", response);

        verify(fileService, times(1)).baixarArquivo("nomeArquivo.jpg", "src/main/resources/static/pedidos/enviadas/", response);
    }

    @Test
    public void testBaixarImagensPedido() throws IOException {
        // Criação de um Pedido fictício com imagens
        Pedido pedido = new Pedido();
        pedido.setId("pedido-id");
        List<String> imagens = Arrays.asList("imagem1.jpg", "imagem2.jpg");
        pedido.setImagens(imagens);

        // Configurar o comportamento do mock do pedidoRepository
        Mockito.when(pedidoRepository.findById("pedido-id")).thenReturn(Optional.of(pedido));

        // Criação de um objeto HttpServletResponse fictício
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        // Chamar o método a ser testado
        pedidoService.baixarImagensPedido("pedido-id", response);

        // Verificar se o método de busca do pedido foi chamado corretamente
        Mockito.verify(pedidoRepository, Mockito.times(1)).findById("pedido-id");

        // Verificar se o método de baixarArquivos do fileService foi chamado corretamente
        Mockito.verify(fileService, Mockito.times(1)).baixarArquivos(imagens, "src/main/resources/static/pedidos/enviadas/", response);
    }

    @Test
    public void testListaPedidosPorRomaneioId() {
        // Criação de um Romaneio fictício
        String romaneioId = "romaneio-id";
        Romaneio romaneio = new Romaneio();
        romaneio.setId(romaneioId);

        // Criação de uma lista fictícia de Pedidos
        List<Pedido> pedidos = new ArrayList<>();
        Pedido pedido1 = new Pedido();
        pedido1.setId("pedido1-id");

        Pedido pedido2 = new Pedido();
        pedido2.setId("pedido2-id");
        pedidos.add(pedido1);
        pedidos.add(pedido2);

        Mockito.when(pedidoRepository.findByRomaneioId(romaneioId)).thenReturn(pedidos);

        List<Pedido> resultado = pedidoService.listaPedidosPorRomaneioId(romaneioId);

        Mockito.verify(pedidoRepository, Mockito.times(1)).findByRomaneioId(romaneioId); // Verificar se o método de busca dos pedidos por romaneioId foi chamado corretamente

        assertEquals(pedidos, resultado); // Verificar se o resultado é igual à lista de pedidos fictícia
    }


    @Test
    public void testCancelaPedido_PedidoJaCancelado() {
        MotivoCancelamentoDTO motivoCancelamentoDTO = new MotivoCancelamentoDTO();
        motivoCancelamentoDTO.setMotivo("Motivo");

        Pedido pedido = new Pedido();
        pedido.setStatus(StatusPedido.CANCELADO);

        // Configurar o comportamento do mock de pedidoRepository
        when(pedidoRepository.findById("1")).thenReturn(Optional.of(pedido));

        catchThrowableOfType(() -> pedidoService.cancelaPedido("1", motivoCancelamentoDTO), RuleOfBusinessException.class);

        verify(pedidoRepository, times(1)).findById("1");    // Verificar se o método de busca do pedido foi chamado corretamente
    }

    @Test
    public void testCancelaPedido_PedidoNaoPendente() {
        MotivoCancelamentoDTO motivoCancelamentoDTO = new MotivoCancelamentoDTO();
        motivoCancelamentoDTO.setMotivo("Motivo");

        Pedido pedido = new Pedido();
        pedido.setStatus(StatusPedido.EM_TRANSITO);

        // Configurar o comportamento do mock de pedidoRepository
        when(pedidoRepository.findById("1")).thenReturn(Optional.of(pedido));

        catchThrowableOfType(() -> pedidoService.cancelaPedido("1", motivoCancelamentoDTO), RuleOfBusinessException.class);

        verify(pedidoRepository, times(1)).findById("1");
    }

    @Test
    public void testCancelaPedido() {
        Pedido pedidoExistente = Mockito.mock(Pedido.class);
        Mockito.when(pedidoExistente.getStatus()).thenReturn(StatusPedido.PENDENTE);

        MotivoCancelamentoDTO motivoCancelamentoDTO = new MotivoCancelamentoDTO();
        motivoCancelamentoDTO.setMotivo("Motivo de cancelamento");

        Mockito.when(pedidoRepository.findById("1")).thenReturn(Optional.of(pedidoExistente));

        pedidoService.cancelaPedido("1", motivoCancelamentoDTO);

        Mockito.verify(pedidoRepository, Mockito.times(1)).findById("1"); // Verificar se o método de busca do pedido foi chamado corretamente

        // Verificar se o método de atualização de status foi chamado corretamente
        Mockito.verify(pedidoExistente, Mockito.times(1)).atualizarStatus(Mockito.eq(StatusPedido.CANCELADO), Mockito.eq(motivoCancelamentoDTO.getMotivo()));
    }

    @Test
    public void testToEntity() {
        PedidoInputDTO dto = new PedidoInputDTO();
        dto.setId("1");
        dto.setItens(new ArrayList<>());
        dto.setFotos(new ArrayList<>());
        dto.setPrioridade(Prioridade.ALTA);
        dto.setAcompanhaStatus(AcompanhaStatus.SIM_AMBOS);

        Pedido pedido = pedidoService.toEntity(dto);

        // Verificar se os atributos do pedido foram definidos corretamente
        assertEquals(dto.getId(), pedido.getId());
        assertEquals(dto.getItens(), pedido.getItens());
        assertEquals(dto.getFotos(), pedido.getImagens());
        assertEquals(dto.getPrioridade(), pedido.getPrioridade());
        assertEquals(dto.getAcompanhaStatus(), pedido.getAcompanhaStatus());

        // Verificar se a data de atualização foi definida corretamente (considerando uma margem de erro de 1 segundo)
        LocalDateTime atual = LocalDateTime.now();
        LocalDateTime dataAtualizacao = pedido.getDataAtualizacao();
        assertTrue(dataAtualizacao.isAfter(atual.minusSeconds(1)));
        assertTrue(dataAtualizacao.isBefore(atual.plusSeconds(1)));
    }


    private static Produto getProduto() {
        Produto produto = new Produto();
        produto.setId("1");
        produto.setCodigo(123);
        produto.setDescricao("Notebook Dell Inspiron 15");
        produto.setPreco(3500.00);
        produto.setPeso(2.0);
        produto.setFabricante("Dell");
        produto.setModelo("Inspiron 15");
        produto.setDataCriacao(LocalDateTime.now());
        produto.setDataAtualizacao(LocalDateTime.now());
        return produto;
    }

    private static Item getItem() {
        ItemPedidoPK pk = new ItemPedidoPK();
        pk.setPedido(PedidoTest.getPedido());
        pk.getPedido().setId("41bc-ab36");
        pk.setProduto(ProdutoTest.getProduto1());
        pk.getProduto().setId("id1");

        Item item = new Item();
        item.setId(pk);
        item.setDescricao("Item teste");
        item.setQuantidade(1);
        item.setPreco(10.0);
        item.setPeso(1.0);
        return item;
    }

    private static Endereco getEndereco() {
        Endereco endereco = new Endereco();
        endereco.setId("11189");
        endereco.setBairro("Centro");
        endereco.setRua("Rua Principal");
        endereco.setCep("12345-678");
        endereco.setNumero(10);
        endereco.setComplemento("Sala 101");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        return endereco;
    }

    private static Funcionario getFuncionarioRemetente() {
        Funcionario remetente = new Funcionario();
        remetente.setId("1");
        remetente.setNome("Remetente");
        remetente.setEmail("remetente@example.com");
        remetente.setFilial(getFilialOrigem());
        return remetente;
    }

    private static Funcionario getFuncionarioDestinatario() {
        Funcionario destinatario = new Funcionario();
        destinatario.setId("2");
        destinatario.setNome("Destinatário");
        destinatario.setEmail("destinatario@example.com");
        destinatario.setFilial(getFilialDestino());
        return destinatario;
    }

    private static Filial getFilialOrigem() {
        Filial origem = new Filial();
        origem.setId(1L);
        origem.setNome("Filial Origem");

        origem.setEndereco(getEndereco());
        return origem;
    }

    private static Filial getFilialDestino() {
        Filial origem = new Filial();
        origem.setId(1L);
        origem.setNome("Filial Destino");
        origem.setEndereco(getEndereco());
        return origem;
    }

    public static Pedido getPedido() {
        Pedido pedido = new Pedido();

        pedido.setId("1");
        pedido.setNumeroPedido(1234);

        // Definindo remetente e destinatário
        pedido.setRemetente(getFuncionarioRemetente());
        pedido.setDestinatario(getFuncionarioDestinatario());

        pedido.setOrigem(getFilialOrigem());
        pedido.setDestino(getFilialDestino());

        // Adicionando imagens
        pedido.getImagens().add("imagem1.jpg");
        pedido.getImagens().add("imagem2.jpg");

        // Definindo outras informações
        pedido.setDataAtualizacao(LocalDateTime.now());
        pedido.setCodigoRastreio("ABC123");
        pedido.setAcompanhaStatus(AcompanhaStatus.SIM_AMBOS);
        pedido.setStatusPedido(StatusPedido.EM_TRANSITO);
        pedido.setPrioridade(Prioridade.ALTA);

        // Adicionando itens ao pedido
        pedido.getItens().add(getItem());

        return pedido;
    }
}