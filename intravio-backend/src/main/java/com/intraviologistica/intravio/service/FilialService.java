package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.FilialDTO;
import com.intraviologistica.intravio.model.Filial;
import com.intraviologistica.intravio.repository.FilialRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilialService {

    private FilialRepository filialRepository;

    public FilialService(FilialRepository filialRepository) {
        this.filialRepository = filialRepository;
    }

    @Transactional
    public List<FilialDTO> listarFiliais() {
        return filialRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Filial findById(Long id) {
        return filialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filial não encontrada: " + id));
    }

    @Transactional
    public Filial buscarFilialPorNome(String nome) {
        return filialRepository.findByNome(nome);
    }

    @Transactional
    public FilialDTO buscarFilialPorId(Long id) {
        return toDTO(findById(id));
    }

    @Transactional
    public Filial salvaFilial(FilialDTO dto) {
        return filialRepository.save(toEntity(dto));
    }

    @Transactional
    public Filial atualizaFilial(FilialDTO dto) {
        Filial filial = findById(dto.getId());
        filial.setId(dto.getId());
        filial.setNome(dto.getNome());
        return filialRepository.save(filial);
    }

    @Transactional
    public void excluiFilial(Long id) {
        filialRepository.deleteById(id);
    }

    public Filial toEntity(FilialDTO filialDTO) {
        Filial filial = new Filial();
        filial.setId(filialDTO.getId());
        filial.setNome(filialDTO.getNome());
        return filial;
    }

    public FilialDTO toDTO(Filial Filial) {
        FilialDTO dto = new FilialDTO();
        dto.setId(Filial.getId());
        dto.setNome(Filial.getNome());
        return dto;
    }
}