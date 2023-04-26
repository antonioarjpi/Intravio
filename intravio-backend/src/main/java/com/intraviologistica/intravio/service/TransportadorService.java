package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.TransportadorDTO;
import com.intraviologistica.intravio.model.Transportador;
import com.intraviologistica.intravio.repository.TransportadorRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransportadorService {

    private TransportadorRepository transportadorRepository;

    public TransportadorService(TransportadorRepository transportadorRepository) {
        this.transportadorRepository = transportadorRepository;
    }

    @Transactional
    public TransportadorDTO buscaTransportadorPorId(Long id) {
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
    public Transportador salva(TransportadorDTO dto) {
        Transportador transportador = toEntity(dto);
        return transportadorRepository.save(transportador);
    }

    @Transactional
    public Transportador atualiza(TransportadorDTO dto) {
        Transportador novoTransportador = findById(dto.getId());

        novoTransportador.setId(dto.getId());
        novoTransportador.setNome(dto.getNome());
        novoTransportador.setMotorista(dto.getMotorista());
        novoTransportador.setPlaca(dto.getPlaca());
        novoTransportador.setVeiculo(dto.getVeiculo());
        novoTransportador.setObservacao(dto.getObservacao());

        return transportadorRepository.save(novoTransportador);
    }

    @Transactional
    public void deleta(Long id) {
        transportadorRepository.deleteById(id);
    }

    @Transactional
    public Transportador findById(Long id) {
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
        return dto;
    }
}
