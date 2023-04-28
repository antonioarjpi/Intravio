package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.TransportadorDTO;
import com.intraviologistica.intravio.model.Transportador;
import com.intraviologistica.intravio.repository.TransportadorRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransportadorService {

    private TransportadorRepository transportadorRepository;

    private static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public TransportadorService(TransportadorRepository transportadorRepository) {
        this.transportadorRepository = transportadorRepository;
    }

    @Transactional
    public TransportadorDTO buscaTransportadorPorId(String id) {
        Transportador transportador = findById(id);
        return toDTO(transportador);
    }

    @Transactional
    public List<TransportadorDTO> listaTodosTransportadores() {
        return transportadorRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Transportador salvaTransportador(TransportadorDTO dto) {
        Transportador transportador = toEntity(dto);
        transportador.setId(getUuid());
        return transportadorRepository.save(transportador);
    }

    @Transactional
    public Transportador atualizaTransportador(TransportadorDTO dto) {
        Transportador novoTransportador = findById(dto.getId());

        novoTransportador.setId(dto.getId());
        novoTransportador.setNome(dto.getNome());
        novoTransportador.setMotorista(dto.getMotorista());
        novoTransportador.setPlaca(dto.getPlaca());
        novoTransportador.setVeiculo(dto.getVeiculo());
        novoTransportador.setCnpj(dto.getCnpj());
        novoTransportador.setObservacao(dto.getObservacao());

        return transportadorRepository.save(novoTransportador);
    }

    @Transactional
    public void deletaTransportador(String id) {
        transportadorRepository.deleteById(id);
    }

    @Transactional
    public Transportador findById(String id) {
        return transportadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transportador não encontrado"));
    }

    public Transportador toEntity(TransportadorDTO dto) {
        Transportador transportador = new Transportador();
        transportador.setNome(dto.getNome());
        transportador.setNome(dto.getNome());
        transportador.setMotorista(dto.getMotorista());
        transportador.setObservacao(dto.getObservacao());
        transportador.setPlaca(dto.getPlaca());
        transportador.setVeiculo(dto.getVeiculo());
        transportador.setCnpj(dto.getCnpj());
        return transportador;
    }

    public TransportadorDTO toDTO(Transportador transportador) {
        TransportadorDTO dto = new TransportadorDTO();
        dto.setId(transportador.getId());
        dto.setNome(transportador.getNome());
        dto.setMotorista(transportador.getMotorista());
        dto.setObservacao(transportador.getObservacao());
        dto.setPlaca(transportador.getPlaca());
        dto.setVeiculo(transportador.getVeiculo());
        dto.setCnpj(transportador.getCnpj());
        return dto;
    }
}
