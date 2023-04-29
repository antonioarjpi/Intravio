package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.FilialDTO;
import com.intraviologistica.intravio.model.Endereco;
import com.intraviologistica.intravio.model.Filial;
import com.intraviologistica.intravio.repository.FilialRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FilialService {

    private FilialRepository filialRepository;

    public FilialService(FilialRepository filialRepository) {
        this.filialRepository = filialRepository;
    }

    private static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
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
        Filial filial = toEntity(dto);
        filial.getEndereco().setId(getUuid());
        return filialRepository.save(filial);
    }

    @Transactional
    public Filial atualizaFilial(FilialDTO dto) {
        Filial filial = findById(dto.getId());

        filial = attFilial(dto, filial);

        return filialRepository.save(filial);
    }

    // Atribui os novos dados que veio DTO na entidade que foi encontrada.
    private Filial attFilial(FilialDTO dto, Filial filial) {
        Endereco endereco = new Endereco();
        endereco.setId(filial.getEndereco().getId());
        endereco.setBairro(dto.getBairro());
        endereco.setRua(dto.getRua());
        endereco.setCep(dto.getCep());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());

        filial.setId(dto.getId());
        filial.setNome(dto.getNome());
        filial.setEndereco(endereco);

        return filial;
    }

    @Transactional
    public void excluiFilial(Long id) {
        filialRepository.deleteById(id);
    }

    public Filial toEntity(FilialDTO filialDTO) {
        Endereco endereco = new Endereco();
        endereco.setBairro(filialDTO.getBairro());
        endereco.setRua(filialDTO.getRua());
        endereco.setCep(filialDTO.getCep());
        endereco.setNumero(filialDTO.getNumero());
        endereco.setComplemento(filialDTO.getComplemento());
        endereco.setCidade(filialDTO.getCidade());
        endereco.setEstado(filialDTO.getEstado());

        Filial filial = new Filial();
        filial.setId(filialDTO.getId());
        filial.setNome(filialDTO.getNome());
        filial.setEndereco(endereco);

        return filial;
    }

    public FilialDTO toDTO(Filial filial) {
        FilialDTO dto = new FilialDTO();
        dto.setId(filial.getId());
        dto.setNome(filial.getNome());
        dto.setRua(filial.getEndereco().getRua());
        dto.setBairro(filial.getEndereco().getBairro());
        dto.setCep(filial.getEndereco().getCep());
        dto.setNumero(filial.getEndereco().getNumero());
        dto.setComplemento(filial.getEndereco().getComplemento());
        dto.setCidade(filial.getEndereco().getCidade());
        dto.setEstado(filial.getEndereco().getEstado());
        return dto;
    }
}