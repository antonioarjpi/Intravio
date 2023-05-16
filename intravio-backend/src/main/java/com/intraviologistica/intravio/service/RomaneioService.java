package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.RomaneioDTO;
import com.intraviologistica.intravio.dto.input.RomaneioFechamentoDTO;
import com.intraviologistica.intravio.dto.input.RomaneioInputDTO;
import com.intraviologistica.intravio.model.Pedido;
import com.intraviologistica.intravio.model.Romaneio;
import com.intraviologistica.intravio.model.Transportador;
import com.intraviologistica.intravio.model.enums.StatusPedido;
import com.intraviologistica.intravio.model.enums.StatusRomaneio;
import com.intraviologistica.intravio.repository.PedidoRepository;
import com.intraviologistica.intravio.repository.RomaneioRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import com.intraviologistica.intravio.service.exceptions.RuleOfBusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RomaneioService {

    private final RomaneioRepository romaneioRepository;
    private final PedidoService pedidoService;
    private final TransportadorService transportadorService;
    private final PedidoRepository pedidoRepository;

    public RomaneioService(RomaneioRepository romaneioRepository, PedidoService pedidoService, TransportadorService transportadorService,
                           PedidoRepository pedidoRepository) {
        this.romaneioRepository = romaneioRepository;
        this.pedidoService = pedidoService;
        this.transportadorService = transportadorService;
        this.pedidoRepository = pedidoRepository;
    }

    private static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public RomaneioInputDTO buscarPorId(String id) {
        Romaneio romaneio = findById(id);
        return toDTO(romaneio);
    }

    @Transactional
    public List<RomaneioDTO> listar(String minDate, String maxDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime hoje = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        LocalDateTime min = minDate.equals("") ? hoje.minusDays(1) : LocalDateTime.parse(minDate+"T00:00:00", formatter);
        LocalDateTime max = maxDate.equals("") ? hoje : LocalDateTime.parse(maxDate+"T23:59:59", formatter);

        return romaneioRepository.findAll(min, max)
                .stream()
                .sorted(Comparator.comparing(Romaneio::getNumeroRomaneio).reversed())
                .map(x -> new RomaneioDTO(x))
                .collect(Collectors.toList());
    }

    @Transactional
    public RomaneioDTO salvar(RomaneioInputDTO romaneioInputDTO) {
        romaneioInputDTO.setId(getUuid());
        Romaneio romaneio = toEntity(romaneioInputDTO);

        insereTransportadorNoRomaneio(romaneioInputDTO, romaneio);

        LocalDateTime now = LocalDateTime.now();
        Integer maxNumeroRomaneio = romaneioRepository.getMaxNumeroRomaneio();

        romaneio.setDataCriacao(now);
        romaneio.setDataAtualizacao(now);
        romaneio.setNumeroRomaneio(maxNumeroRomaneio + 1);

        if (romaneioInputDTO.isProcessa()) {
            romaneio.setStatus(StatusRomaneio.EM_TRANSITO);
        } else {
            romaneio.setStatus(StatusRomaneio.ABERTO);
        }

        validatePedidos(romaneioInputDTO.getPedidos(), romaneio.getId());

        romaneio = romaneioRepository.save(romaneio);

        adicionaPedidosNoRomaneio(romaneioInputDTO, romaneio);

        return new RomaneioDTO(romaneio);
    }

    @Transactional
    public Romaneio AtualizarRomaneio(RomaneioInputDTO dto) {
        Romaneio romaneio = findById(dto.getId());

        if (romaneio.getStatus().ordinal() == 3) {
            throw new RuleOfBusinessException("Romaneio fechado, não pode ser alterado");
        } else if (romaneio.getStatus().ordinal() == 2 && !dto.isProcessa()) {
            throw new RuleOfBusinessException("Romaneio em trânsito, não pode ser alterado");
        }

        if (dto.isProcessa()) {
            romaneio.setStatus(StatusRomaneio.EM_TRANSITO);
        } else {
            romaneio.setStatus(StatusRomaneio.ABERTO);
        }

        insereTransportadorNoRomaneio(dto, romaneio);

        attRomaneio(dto, romaneio);

        removePedidosDoRomaneio(dto, romaneio);

        adicionaPedidosNoRomaneio(dto, romaneio);

        romaneio = romaneioRepository.save(romaneio);

        return romaneio;
    }

    // Exclui o Romaneio se status não for igual a "FECHADO"
    @Transactional
    public void excluiRomaneio(String id) {
        Romaneio romaneio = findById(id);

        // Se Status do Romaneio for igual a fechado, retorna um Erro
        if (romaneio.getStatus().ordinal() == 3) {
            throw new RuleOfBusinessException("Romaneio com status de fechado não pode ser excluído.");
        }

        // Remove o romaneio dos pedidos antes de excluir o romaneio
        List<Pedido> pedidos = pedidoService.listaPedidosPorRomaneioId(id);
        if (!pedidos.isEmpty()) {
            for (Pedido pedido : pedidos) {
                pedido.setRomaneio(null);
                pedido.atualizarStatus(StatusPedido.PENDENTE, "Pedido retornou para filial de origem");

                pedidoService.salvaPedidoRetornandoEntidade(pedido);
            }
        }

        romaneioRepository.deleteById(id);
    }

    @Transactional
    public void fecharRomaneio(RomaneioFechamentoDTO dto) {
        Romaneio romaneio = findById(dto.getRomaneioId());
        romaneio.setStatus(StatusRomaneio.FECHADO);
        romaneio = romaneioRepository.save(romaneio);

        List<Integer> pedidosRomaneio = new ArrayList<>();
        for (Pedido pedido : romaneio.getPedidos()) {
            pedidosRomaneio.add(pedido.getNumeroPedido());
        }

        atualizaStatusPedidos(dto.getPedidosConcluido(), pedidosRomaneio, 7);
        atualizaStatusPedidos(dto.getPedidosRetorno(), pedidosRomaneio, 8);
    }

    private void atualizaStatusPedidos(List<Integer> dto, List<Integer> pedidosRomaneio, int status) {
        for (Integer numeroPedido : dto) {
            if (pedidosRomaneio.contains(numeroPedido)) {
                Pedido pedido = pedidoService.buscaPorNumeroPedido(numeroPedido);
                atualizarStatusDoPedido(pedido, status);
            }
        }
    }

    @Transactional
    public void alterarStatusDeTodosPedidosParaEmTransito(String id, Integer status) {
        Romaneio romaneio = findById(id);
        List<Pedido> pedidos = pedidoService.listaPedidosPorRomaneioId(id);

        // Atualiza status de todos os pedidos do romaneio
        for (Pedido pedido : pedidos) {
            atualizarStatusDoPedido(pedido, status);
        }

        atualizarStatusDoRomaneio(romaneio, status);
    }

    private void atualizarStatusDoPedido(Pedido pedido, Integer status) {
        String mensagem = null;
        StatusPedido novoStatus = null;

        switch (status) {
            case 0 -> {
                novoStatus = StatusPedido.SEPARADO;
                mensagem = "Objeto separado na unidade " + pedido.getOrigem().getNome().toUpperCase(Locale.ROOT);
            }
            case 1 -> {
                novoStatus = StatusPedido.ENTREGUE_AO_TRANSPORTADOR;
                mensagem = "Objeto entregue ao transportador";
            }
            case 2 -> {
                novoStatus = StatusPedido.EM_TRANSITO;
                mensagem = "Objeto encaminhado para filial " + pedido.getDestino().getNome().toUpperCase(Locale.ROOT);
            }
            case 7 -> {
                novoStatus = StatusPedido.ENTREGUE;
                mensagem = "Objeto recebido pelo destinatário";
            }
            case 8 -> {
                novoStatus = StatusPedido.RETORNO;
                mensagem = "Objeto retornou";
            }
            default -> throw new RuleOfBusinessException("Não foi possível encontrar status: " + status);
        }

        pedido.atualizarStatus(novoStatus, mensagem);
    }

    private void atualizarStatusDoRomaneio(Romaneio romaneio, Integer status) {
        switch (status) {
            case 0 -> romaneio.setStatus(StatusRomaneio.ABERTO);
            case 1 -> romaneio.setStatus(StatusRomaneio.PROCESSADO);
            case 2 -> romaneio.setStatus(StatusRomaneio.EM_TRANSITO);
            case 3 -> romaneio.setStatus(StatusRomaneio.FECHADO);
            default -> throw new RuleOfBusinessException("Não foi possível encontrar status: " + status);
        }
    }

    // Busca Romaneio Retornando a Entidade
    @Transactional
    public Romaneio findById(String id) {
        return romaneioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Romaneio não encontrado."));
    }

    // Valida se os pedidos atendem os requisitos antes de inserir no romaneio
    private void validatePedidos(List<Integer> pedidos, String romaneioId) {
        for (Integer idPedido : pedidos) {
            Pedido pedido = pedidoService.buscaPorNumeroPedido(idPedido);
            if (pedido.getStatus() == StatusPedido.CANCELADO) {
                throw new RuleOfBusinessException("Não é possível adicionar pedido cancelado em romaneio. " +
                        "Por favor, remova o pedido " + idPedido + " do romaneio.");
            } else if (pedido.getStatus() != StatusPedido.PENDENTE && pedido.getRomaneio().getId() != romaneioId) {
                throw new RuleOfBusinessException("Pedido " + pedido.getId() + " já possui romaneio e não pode ser adicionado");
            }
        }
    }

    // Compara e remove os pedidos que não existe no romaneioDTO
    private void removePedidosDoRomaneio(RomaneioInputDTO romaneioInputDTO, Romaneio romaneio) {
        List<Integer> list = romaneio.getPedidos().stream().map(Pedido::getNumeroPedido).collect(Collectors.toList());
        List<Integer> pedidos = romaneioInputDTO.getPedidos();

        List<Integer> pedidosNaoRepetidos = Stream.concat(list.stream(), pedidos.stream())
                .filter(n -> !list.contains(n) || !pedidos.contains(n))
                .collect(Collectors.toList());

        for (Integer id : pedidosNaoRepetidos) {
            Pedido pedido = pedidoService.buscaPorNumeroPedido(id);
            pedido.setRomaneio(null);
            pedido.setStatus(StatusPedido.PENDENTE);
        }
    }

    // Adiciona os pedidos DTO no romaneio
    private void adicionaPedidosNoRomaneio(RomaneioInputDTO dto, Romaneio romaneio) {
        List<Pedido> pedidos = new ArrayList<>();

        for (Integer idPedido : dto.getPedidos()) {
            Pedido pedido = pedidoService.buscaPorNumeroPedido(idPedido);
            pedido.setRomaneio(romaneio);
            if (dto.isProcessa()) {
                pedido.atualizarStatus(StatusPedido.EM_TRANSITO, "Pedido encaminhado para filial " +
                        pedido.getDestino().getNome().toUpperCase(Locale.ROOT));
            } else {
                pedido.atualizarStatus(StatusPedido.SEPARADO, "Pedido separado na unidade " +
                        pedido.getOrigem().getNome().toUpperCase(Locale.ROOT));
            }
            pedido = pedidoService.salvaPedidoRetornandoEntidade(pedido);

            pedidos.add(pedido);
        }

        romaneio.setPedidos(pedidos);
    }

    // Busca e atribuir transportador no Romaneio
    private void insereTransportadorNoRomaneio(RomaneioInputDTO romaneioInputDTO, Romaneio romaneio) {
        Transportador transportador = transportadorService.findById(romaneioInputDTO.getTransportadorCodigo());
        romaneio.setTransportador(transportador);
    }

    // Atribui DTO na entidade Romaneio
    private static void attRomaneio(RomaneioInputDTO romaneioInputDTO, Romaneio romaneio) {
        romaneio.setId(romaneio.getId());
        romaneio.setDataAtualizacao(LocalDateTime.now());
        romaneio.setTaxaFrete(romaneioInputDTO.getTaxaFrete());
        romaneio.setObservacao(romaneioInputDTO.getObservacao());
    }

    // Converte a entidade para DTO
    public RomaneioInputDTO toDTO(Romaneio romaneio) {
        RomaneioInputDTO dto = new RomaneioInputDTO();

        dto.setId(romaneio.getId());
        dto.setPedidos(romaneio.getPedidos().stream().map(x -> x.getNumeroPedido()).collect(Collectors.toList()));
        dto.setTransportadorCodigo(romaneio.getTransportador().getId());
        dto.setTaxaFrete(romaneio.getTaxaFrete());
        dto.setDataCriacao(romaneio.getDataCriacao());
        dto.setDataAtualizacao(romaneio.getDataAtualizacao());
        dto.setObservacao(romaneio.getObservacao());
        dto.setNumeroRomaneio(romaneio.getNumeroRomaneio());

        return dto;
    }

    // Converte o DTO para Entidade
    public Romaneio toEntity(RomaneioInputDTO dto) {
        Romaneio romaneio = new Romaneio();

        romaneio.setId(dto.getId());
        romaneio.setDataAtualizacao(dto.getDataAtualizacao());
        romaneio.setTaxaFrete(dto.getTaxaFrete() == null ? 00.0 : dto.getTaxaFrete());
        romaneio.setObservacao(dto.getObservacao());

        return romaneio;
    }

    @Transactional
    public void processarRomaneio(String id) {
        Romaneio romaneio = findById(id);
        atualizarStatusDoRomaneio(romaneio, 2);
        romaneioRepository.save(romaneio);
        for (Pedido pedido : romaneio.getPedidos()) {
            atualizarStatusDoPedido(pedido, 2);
            pedidoService.salvaPedidoRetornandoEntidade(pedido);
        }
    }
}
