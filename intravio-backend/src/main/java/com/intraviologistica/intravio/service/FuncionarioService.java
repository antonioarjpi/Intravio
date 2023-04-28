package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.FuncionarioDTO;
import com.intraviologistica.intravio.model.Departamento;
import com.intraviologistica.intravio.model.Funcionario;
import com.intraviologistica.intravio.repository.FuncionarioRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final DepartamentoService departamentoService;

    private static String getUuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public FuncionarioService(FuncionarioRepository funcionarioRepository, DepartamentoService departamentoService) {
        this.funcionarioRepository = funcionarioRepository;
        this.departamentoService = departamentoService;
    }

    @Transactional
    public List<FuncionarioDTO> listaFuncionarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        return funcionarios.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public Funcionario findById(String id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
    }

    @Transactional
    public Funcionario buscaFuncionarioPorEmail(String email) {
        return funcionarioRepository.findByEmail(email);
    }

    @Transactional
    public FuncionarioDTO buscaFuncionarioPorId(String id) {
        return toDTO(findById(id));
    }

    @Transactional
    public FuncionarioDTO salvaFuncionario(FuncionarioDTO funcionarioDTO) {
        Funcionario funcionario = toEntity(funcionarioDTO);
        buscarEInserirDepartamento(funcionarioDTO, funcionario);
        funcionario.setId(getUuid());
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);
        return toDTO(funcionarioSalvo);
    }

    @Transactional
    public FuncionarioDTO atualizaFuncionario(String id, FuncionarioDTO funcionarioDTO) {
        Funcionario funcionario = findById(id);
        funcionario.setNome(funcionarioDTO.getNome());
        funcionario.setEmail(funcionarioDTO.getEmail());
        buscarEInserirDepartamento(funcionarioDTO, funcionario);
        Funcionario updatedFuncionario = funcionarioRepository.save(funcionario);
        return toDTO(updatedFuncionario);
    }

    private void buscarEInserirDepartamento(FuncionarioDTO funcionarioDTO, Funcionario funcionario) {
        Departamento departamento = departamentoService.buscarDepartamentoPorNome(funcionarioDTO.getDepartamentoNome());
        funcionario.setDepartamento(departamento);
    }

    @Transactional
    public void deletarFuncionario(String id) {
        funcionarioRepository.deleteById(id);
    }

    private FuncionarioDTO toDTO(Funcionario funcionario) {
        FuncionarioDTO funcionarioDTO = new FuncionarioDTO();
        funcionarioDTO.setId(funcionario.getId());
        funcionarioDTO.setNome(funcionario.getNome());
        funcionarioDTO.setEmail(funcionario.getEmail());
        funcionarioDTO.setDepartamentoNome(funcionario.getDepartamento() != null ? funcionario.getDepartamento().getNome() : null);
        return funcionarioDTO;
    }

    public Funcionario toEntity(FuncionarioDTO funcionarioDTO) {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(funcionarioDTO.getId());
        funcionario.setNome(funcionarioDTO.getNome());
        funcionario.setEmail(funcionarioDTO.getEmail());
        return funcionario;
    }
}
