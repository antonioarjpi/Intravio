package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.RomaneioDTO;
import com.intraviologistica.intravio.dto.input.RomaneioFechamentoDTO;
import com.intraviologistica.intravio.dto.input.RomaneioInputDTO;
import com.intraviologistica.intravio.model.*;
import com.intraviologistica.intravio.model.enums.AcompanhaStatus;
import com.intraviologistica.intravio.model.enums.Prioridade;
import com.intraviologistica.intravio.model.enums.StatusPedido;
import com.intraviologistica.intravio.model.enums.StatusRomaneio;
import com.intraviologistica.intravio.repository.PedidoRepository;
import com.intraviologistica.intravio.repository.RomaneioRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RomaneioServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private FilialService filialService;

    @Mock
    private FuncionarioService funcionarioService;

    @Mock
    private TransportadorService transportadorService;

    @Mock
    private ItemService itemService;

    @Mock
    private FileService fileService;

    @Mock
    private PedidoService pedidoService;

    @Mock
    private RomaneioRepository romaneioRepository;

    @InjectMocks
    private RomaneioService romaneioService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBuscarPorId() {
        String romaneioId = "1";
        Romaneio romaneio = getRomaneio();
        RomaneioInputDTO romaneioInputDTO = romaneioService.toDTO(romaneio);
        romaneioInputDTO.setProcessa(false);

        when(romaneioRepository.findById(romaneioId)).thenReturn(Optional.of(romaneio));

        RomaneioInputDTO romaneioEncontrado = romaneioService.buscarPorId(romaneioId);

        verify(romaneioRepository, times(1)).findById(romaneioId);

        assertEquals(romaneioInputDTO.getPedidos(), romaneioEncontrado.getPedidos());
        assertEquals(romaneioInputDTO.getNumeroRomaneio(), romaneioEncontrado.getNumeroRomaneio());
        assertEquals(romaneioInputDTO.getId(), romaneioEncontrado.getId());
        assertEquals(romaneioInputDTO.getTaxaFrete(), romaneioEncontrado.getTaxaFrete());
        assertEquals(romaneioInputDTO.getDataAtualizacao(), romaneioEncontrado.getDataAtualizacao());
        assertEquals(romaneioInputDTO.getObservacao(), romaneioEncontrado.getObservacao());
        assertEquals(romaneioInputDTO.getTransportadorCodigo(), romaneioEncontrado.getTransportadorCodigo());
        assertEquals(romaneioInputDTO.getDataCriacao(), romaneioEncontrado.getDataCriacao());
        assertFalse(romaneioInputDTO.isProcessa());
    }

    @Test
    public void testListar() {
        String minDate = "2023-05-01";
        String maxDate = "2023-05-31";

        List<Romaneio> romaneios = new ArrayList<>();
        romaneios.add(getRomaneio());

        when(romaneioRepository.findAll(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(romaneios);

        List<RomaneioDTO> romaneiosListados = romaneioService.listar(minDate, maxDate);

        verify(romaneioRepository, times(1)).findAll(any(LocalDateTime.class), any(LocalDateTime.class));
        assertEquals(1, romaneiosListados.size());
        assertEquals(romaneios.get(0).getId(), romaneiosListados.get(0).getId());
        assertEquals(romaneios.get(0).getNumeroRomaneio(), romaneiosListados.get(0).getNumeroRomaneio());
        assertEquals(romaneios.get(0).getStatus().ordinal(), romaneiosListados.get(0).getStatusRomaneio());
        assertEquals(romaneios.get(0).getPedidos().get(0).getNumeroPedido(), romaneiosListados.get(0).getPedidos().get(0).getNumeroPedido());
        assertEquals(romaneios.get(0).getDataCriacao(), romaneiosListados.get(0).getDataCriacao());
        assertEquals(romaneios.get(0).getDataAtualizacao(), romaneiosListados.get(0).getDataAtualizacao());
        assertEquals(romaneios.get(0).getValorTotal(), romaneiosListados.get(0).getValorCarga());
        assertEquals(romaneios.get(0).getPesoTotal(), romaneiosListados.get(0).getPesoCarga());
        assertEquals(romaneios.get(0).getTransportador().getPlaca(), romaneiosListados.get(0).getPlaca());
        assertEquals(romaneios.get(0).getTransportador().getNome(), romaneiosListados.get(0).getTransportadora());
        assertEquals(romaneios.get(0).getTransportador().getMotorista(), romaneiosListados.get(0).getMotorista());
        assertEquals(romaneios.get(0).getPedidos().size(), romaneiosListados.get(0).getQuantidadePedidos());
        assertEquals(romaneios.get(0).getTransportador().getPlaca(), romaneiosListados.get(0).getPlaca());
        assertEquals(romaneios.get(0).getTransportador().getMotorista(), romaneiosListados.get(0).getMotorista());
    }

    @Test
    public void testFindById_RomaneioExistente() {
        String id = "1";

        Romaneio romaneio = getRomaneio();

        when(romaneioRepository.findById(id)).thenReturn(Optional.of(romaneio));

        Romaneio romaneioEncontrado = romaneioService.findById(id);

        verify(romaneioRepository, times(1)).findById(id);

        assertNotNull(romaneioEncontrado);
        assertEquals(id, romaneioEncontrado.getId());
    }

    @Test
    public void testFindById_RomaneioNaoEncontrado() {
        String id = "1";

        when(romaneioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> romaneioService.findById(id));

        verify(romaneioRepository, times(1)).findById(id);
    }

    @Test
    public void testSalvar() {
        RomaneioInputDTO romaneioInputDTO = new RomaneioInputDTO();
        romaneioInputDTO.setId("1");
        romaneioInputDTO.setNumeroRomaneio(1);
        romaneioInputDTO.setPedidos(List.of(getPedido().getNumeroPedido()));
        romaneioInputDTO.setObservacao("Nova Observação");
        romaneioInputDTO.setTaxaFrete(20.0);
        romaneioInputDTO.setDataCriacao(LocalDateTime.now());
        romaneioInputDTO.setProcessa(true);
        romaneioInputDTO.setTransportadorCodigo(TransportadorTest.getTransportador().getId());

        Transportador transportador = TransportadorTest.getTransportador();

        when(transportadorService.findById(romaneioInputDTO.getTransportadorCodigo())).thenReturn(transportador);

        Pedido pedido = getPedido();
        when(pedidoService.buscaPorNumeroPedido(1)).thenReturn(pedido);
        when(pedidoService.salvaPedidoRetornandoEntidade(any(Pedido.class))).thenReturn(pedido);

        when(romaneioRepository.getMaxNumeroRomaneio()).thenReturn(10);
        when(romaneioRepository.save(any(Romaneio.class))).thenReturn(getRomaneio());

        RomaneioDTO romaneioDTO = romaneioService.salvar(romaneioInputDTO);

        verify(transportadorService, times(1)).findById(romaneioInputDTO.getTransportadorCodigo());

        verify(pedidoService, times(2)).buscaPorNumeroPedido(1);

        verify(romaneioRepository, times(1)).save(any(Romaneio.class));

        assertNotNull(romaneioDTO);
        assertEquals(romaneioDTO.getPedidos().size(), 1);
        assertEquals(romaneioDTO.getNumeroRomaneio(), 1);
        assertEquals(romaneioDTO.getTaxaFrete(), 20.0);
        assertEquals(romaneioDTO.getObservacao(), "Nova Observação");
        assertEquals(romaneioDTO.getStatusRomaneio(), StatusRomaneio.ABERTO.ordinal());
    }

    @Test
    public void testAtualizarRomaneio() {
        RomaneioInputDTO romaneioInputDTO = new RomaneioInputDTO();
        romaneioInputDTO.setId("1");
        romaneioInputDTO.setNumeroRomaneio(1);
        romaneioInputDTO.setPedidos(List.of(getPedido().getNumeroPedido()));
        romaneioInputDTO.setObservacao("Nova Observação");
        romaneioInputDTO.setTaxaFrete(20.0);
        romaneioInputDTO.setDataCriacao(LocalDateTime.now());
        romaneioInputDTO.setProcessa(true);
        romaneioInputDTO.setTransportadorCodigo(TransportadorTest.getTransportador().getId());

        Romaneio romaneioExistente = getRomaneio();

        when(romaneioRepository.findById(romaneioInputDTO.getId())).thenReturn(Optional.of(romaneioExistente));
        when(romaneioRepository.save(any(Romaneio.class))).thenReturn(getRomaneio());
        when(pedidoService.buscaPorNumeroPedido(1)).thenReturn(getPedido());
        when(pedidoService.salvaPedidoRetornandoEntidade(any(Pedido.class))).thenReturn(getPedido());

        Romaneio romaneioAtualizado = romaneioService.AtualizarRomaneio(romaneioInputDTO);

        verify(romaneioRepository, times(1)).findById(romaneioInputDTO.getId());

        ArgumentCaptor<Romaneio> romaneioCaptor = ArgumentCaptor.forClass(Romaneio.class);
        verify(romaneioRepository, times(1)).save(romaneioCaptor.capture());
        Romaneio romaneioSalvo = romaneioCaptor.getValue();

        assertNotNull(romaneioAtualizado);
    }

    @Test
    public void testExcluiRomaneio() {
        String romaneioId = "12345";

        Romaneio romaneioExistente = getRomaneio();

        when(romaneioRepository.findById(romaneioId)).thenReturn(Optional.of(romaneioExistente));

        // Configurar comportamento do mock do serviço de pedidos
        when(pedidoService.listaPedidosPorRomaneioId(romaneioId)).thenReturn(List.of(getPedido()));
        when(pedidoService.salvaPedidoRetornandoEntidade(any(Pedido.class))).thenReturn(getPedido());

        romaneioService.excluiRomaneio(romaneioId);

        verify(romaneioRepository, times(1)).findById(romaneioId);

        verify(pedidoService, times(1)).listaPedidosPorRomaneioId(romaneioId);

        verify(pedidoService, times(1)).salvaPedidoRetornandoEntidade(any(Pedido.class));

        ArgumentCaptor<String> romaneioIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(romaneioRepository, times(1)).deleteById(romaneioIdCaptor.capture());
        String deletedRomaneioId = romaneioIdCaptor.getValue();
        assertEquals(romaneioId, deletedRomaneioId);
    }

    @Test
    public void testProcessarRomaneio() {
        String romaneioId = "1";

        Romaneio romaneioExistente = getRomaneio();

        when(romaneioRepository.findById(romaneioId)).thenReturn(Optional.of(romaneioExistente));
        when(romaneioRepository.save(any(Romaneio.class))).thenReturn(getRomaneio());

        romaneioService.processarRomaneio(romaneioId);

        verify(romaneioRepository, times(1)).findById(romaneioId);

        ArgumentCaptor<Romaneio> romaneioCaptor = ArgumentCaptor.forClass(Romaneio.class);
        verify(romaneioRepository, times(1)).save(romaneioCaptor.capture());
        Romaneio romaneioSalvo = romaneioCaptor.getValue();

        assertNotNull(romaneioSalvo);
        assertNotNull(romaneioSalvo.getId());

        verify(pedidoService, times(1)).salvaPedidoRetornandoEntidade(any(Pedido.class));
    }

    @Test
    public void testFecharRomaneio() {
        RomaneioFechamentoDTO dto = new RomaneioFechamentoDTO();
        dto.setRomaneioId("romaneioId");
        dto.setPedidosConcluido(Arrays.asList(1, 2, 3));
        dto.setPedidosRetorno(Arrays.asList(4, 5));

        Romaneio romaneio = getRomaneio();

        Pedido pedido1 = getPedido();
        pedido1.setNumeroPedido(1);
        pedido1.setRomaneio(romaneio);

        Pedido pedido2 = getPedido();
        pedido2.setNumeroPedido(2);
        pedido2.setRomaneio(romaneio);

        Pedido pedido3 = getPedido();
        pedido3.setNumeroPedido(3);
        pedido3.setRomaneio(romaneio);

        Pedido pedido4 = getPedido();
        pedido4.setNumeroPedido(4);
        pedido4.setRomaneio(romaneio);

        Pedido pedido5 = getPedido();
        pedido5.setNumeroPedido(5);
        pedido5.setRomaneio(romaneio);

        romaneio.setPedidos(List.of(pedido1, pedido2, pedido3, pedido4, pedido5));

        List<Pedido> pedidos = Arrays.asList(pedido1, pedido2, pedido3, pedido4, pedido5);

        when(romaneioRepository.findById(dto.getRomaneioId())).thenReturn(Optional.of(romaneio));
        when(romaneioRepository.save(romaneio)).thenReturn(romaneio);

        when(pedidoService.buscaPorNumeroPedido(anyInt())).thenAnswer(invocation -> {
            int numeroPedido = invocation.getArgument(0);
            Pedido pedidoMock = pedidos.stream()
                    .filter(pedido -> pedido.getNumeroPedido() == numeroPedido)
                    .findFirst()
                    .orElse(null);
            return pedidoMock;
        });

        romaneioService.fecharRomaneio(dto);

        // Verificações
        verify(romaneioRepository).findById(dto.getRomaneioId());
        verify(romaneioRepository).save(romaneio);

        for (Pedido pedido : pedidos) {
            verify(pedidoService).buscaPorNumeroPedido(pedido.getNumeroPedido());
        }

        assertEquals(StatusRomaneio.FECHADO, romaneio.getStatus());

        for (Integer numeroPedido : dto.getPedidosConcluido()) {
            Pedido pedido = pedidos.stream()
                    .filter(p -> p.getNumeroPedido() == numeroPedido)
                    .findFirst()
                    .orElse(null);
            assertNotNull(pedido);
            assertEquals(StatusPedido.ENTREGUE, pedido.getStatus());
            assertEquals("Objeto recebido pelo destinatário", pedido.getHistoricoPedidos().get(0).getComentario());
        }

        for (Integer numeroPedido : dto.getPedidosRetorno()) {
            Pedido pedido = pedidos.stream()
                    .filter(p -> p.getNumeroPedido() == numeroPedido)
                    .findFirst()
                    .orElse(null);
            assertNotNull(pedido);
            assertEquals(StatusPedido.RETORNO, pedido.getStatus());
            assertEquals("Objeto retornou", pedido.getHistoricoPedidos().get(0).getComentario());
        }
    }

    private static Romaneio getRomaneio() {
        Romaneio romaneio = new Romaneio();
        romaneio.setId("1");
        romaneio.setNumeroRomaneio(1);
        romaneio.setTransportador(TransportadorTest.getTransportador());
        romaneio.setStatus(StatusRomaneio.ABERTO);
        romaneio.setTaxaFrete(20.0);
        romaneio.setPedidos(List.of(getPedido()));
        romaneio.setObservacao("Nova Observação");
        LocalDateTime now = LocalDateTime.now();
        romaneio.setDataAtualizacao(now);
        romaneio.setDataCriacao(now);
        return romaneio;
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
        pedido.setNumeroPedido(1);

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
        pedido.setStatusPedido(StatusPedido.PENDENTE);
        pedido.setPrioridade(Prioridade.ALTA);

        // Adicionando itens ao pedido
        pedido.getItens().add(getItem());

        return pedido;
    }
}