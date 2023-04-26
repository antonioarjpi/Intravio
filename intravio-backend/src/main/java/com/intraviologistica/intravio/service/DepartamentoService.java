package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.DepartamentoDTO;
import com.intraviologistica.intravio.model.Departamento;
import com.intraviologistica.intravio.repository.DepartamentoRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartamentoService {

    private DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Transactional
    public List<DepartamentoDTO> listarDepartamentos() {
        return departamentoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Departamento buscarDepartamentoPorNome(String nome) {
        return departamentoRepository.findByNomeIgnoreCase(nome);
    }

    @Transactional
    public Departamento buscarDepartamentoPorId(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com id: " + id));
    }

    @Transactional
    public DepartamentoDTO buscarDepartamentoPorIdRetornandoDTO(Long id){
        return toDTO(buscarDepartamentoPorId(id));
    }

    @Transactional
    public Departamento salvarDepartamento(DepartamentoDTO dto) {
        Departamento departamento = toEntity(dto);
        return departamentoRepository.save(departamento);
    }

    @Transactional
    public Departamento atualizarDepartamento(Long id, DepartamentoDTO departamentoAtualizado) {
        Departamento departamento = buscarDepartamentoPorId(id);
        departamento.setNome(departamentoAtualizado.getNome());
        return departamentoRepository.save(departamento);
    }

    @Transactional
    public void excluirDepartamento(Long id) {
        departamentoRepository.deleteById(id);
    }

    public Departamento toEntity(DepartamentoDTO dto){
        Departamento departamento = new Departamento();
        departamento.setId(dto.getId());
        departamento.setNome(dto.getNome());
        return departamento;
    }

    public DepartamentoDTO toDTO(Departamento departamento){
        DepartamentoDTO departamentoDTO = new DepartamentoDTO();
        departamentoDTO.setId(departamento.getId());
        departamentoDTO.setNome(departamento.getNome());
        return departamentoDTO;
    }
}
