package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.RomaneioDTO;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RomaneioService {

    private final RomaneioRepository romaneioRepository;
    private final PedidoService pedidoService;
    private final TransportadorService transportadorService;
    private final PedidoRepository pedidoRepository;

    public RomaneioService(RomaneioRepository romaneioRepository, PedidoService pedidoService, TransportadorService transportadorService, PedidoRepository pedidoRepository) {
        this.romaneioRepository = romaneioRepository;
        this.pedidoService = pedidoService;
        this.transportadorService = transportadorService;
        this.pedidoRepository = pedidoRepository;
    }

    public RomaneioDTO buscarPorId(Long id) {
        Romaneio romaneio = findById(id);
        return toDTO(romaneio);
    }

    @Transactional
    public List<RomaneioDTO> listar() {
        return romaneioRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Romaneio salvar(RomaneioDTO romaneioDTO) {
        Romaneio romaneio = toEntity(romaneioDTO);

        insereTransportadorNoRomaneio(romaneioDTO, romaneio);

        LocalDateTime now = LocalDateTime.now();

        romaneio.setStatus(StatusRomaneio.ABERTO);
        romaneio.setDataCriacao(now);
        romaneio.setDataAtualizacao(now);

        romaneio = romaneioRepository.save(romaneio);

        adicionaPedidos(romaneioDTO, romaneio);

        return romaneio;
    }


    @Transactional
    public Romaneio AtualizarRomaneio(RomaneioDTO romaneioDTO) {
        Romaneio romaneio = findById(romaneioDTO.getId());

        insereTransportadorNoRomaneio(romaneioDTO, romaneio);

        romaneio.setId(romaneio.getId());
        romaneio.setDataAtualizacao(LocalDateTime.now());
        romaneio.setTaxaFrete(romaneio.getTaxaFrete());
        romaneio.setObservacao(romaneioDTO.getObservacao());

        romaneio = romaneioRepository.save(romaneio);

        adicionaPedidos(romaneioDTO, romaneio);

        return romaneio;
    }

    @Transactional
    public void excluiRomaneio(Long id) {
        findById(id);
        Pedido pedido = pedidoRepository.findByRomaneioId(id);
        if (pedido != null) {
            pedido.setRomaneio(null);
            pedido.setStatus(StatusPedido.PENDENTE);
            pedidoRepository.save(pedido);
        }
        romaneioRepository.deleteById(id);
    }

    @Transactional
    public Romaneio findById(Long id) {
        return romaneioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Romaneio não encontrado."));
    }

    private void insereTransportadorNoRomaneio(RomaneioDTO romaneioDTO, Romaneio romaneio) {
        Transportador transportador = transportadorService.findById(romaneioDTO.getTransportadorCodigo());
        romaneio.setTransportador(transportador);
    }

    private void adicionaPedidos(RomaneioDTO romaneioDTO, Romaneio romaneio) {
        for (Long idPedido : romaneioDTO.getPedidos()) {
            Pedido pedido = pedidoService.findById(idPedido);
            if (pedido.getStatus().name() != "PENDENTE" && pedido.getRomaneio().getId() != romaneio.getId()) {
                throw new RuleOfBusinessException("Pedido " + pedido.getId() + " já possui romaneio e não pode ser adicionado");
            }
            pedido.setRomaneio(romaneio);
            pedido.setStatus(StatusPedido.EM_TRANSITO);
        }

        pedidoRepository.saveAll(romaneio.getPedidos());
    }

    public RomaneioDTO toDTO(Romaneio romaneio) {
        RomaneioDTO dto = new RomaneioDTO();

        dto.setId(romaneio.getId());
        dto.setPedidos(romaneio.getPedidos().stream().map(x -> x.getId()).collect(Collectors.toList()));
        dto.setTransportadorCodigo(romaneio.getTransportador().getId());
        dto.setStatus(romaneio.getStatus());
        dto.setTaxaFrete(romaneio.getTaxaFrete());
        dto.setDataCriacao(romaneio.getDataCriacao());
        dto.setDataAtualizacao(romaneio.getDataAtualizacao());
        dto.setObservacao(romaneio.getObservacao());

        return dto;
    }

    public Romaneio toEntity(RomaneioDTO dto) {
        Romaneio romaneio = new Romaneio();

        romaneio.setId(dto.getId());
        romaneio.setDataAtualizacao(dto.getDataAtualizacao());
        romaneio.setStatus(dto.getStatus());
        romaneio.setTaxaFrete(dto.getTaxaFrete());
        romaneio.setObservacao(dto.getObservacao());

        return romaneio;
    }
}
