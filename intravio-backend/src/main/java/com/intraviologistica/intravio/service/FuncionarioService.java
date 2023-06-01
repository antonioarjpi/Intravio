package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.dto.FuncionarioDTO;
import com.intraviologistica.intravio.dto.input.FuncionarioInputDTO;
import com.intraviologistica.intravio.model.Departamento;
import com.intraviologistica.intravio.model.Filial;
import com.intraviologistica.intravio.model.Funcionario;
import com.intraviologistica.intravio.repository.DepartamentoRepository;
import com.intraviologistica.intravio.repository.FilialRepository;
import com.intraviologistica.intravio.repository.FuncionarioRepository;
import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final DepartamentoRepository departamentoRepository;
    private final FilialRepository filialRepository;

    private static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public FuncionarioService(FuncionarioRepository funcionarioRepository, DepartamentoRepository departamentoRepository, FilialRepository filialRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.departamentoRepository = departamentoRepository;
        this.filialRepository = filialRepository;
    }

    @Transactional
    public List<FuncionarioDTO> listaFuncionarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        return funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .map(x -> new FuncionarioDTO(x))
                .collect(Collectors.toList());
    }

    @Transactional
    public Funcionario findById(String id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
    }

    @Transactional
    public Funcionario buscaFuncionarioPorEmail(String email) {
        Funcionario funcionario = funcionarioRepository.findByEmail(email);
        if (funcionario == null) {
            throw new ResourceNotFoundException("Funcionário não encontrado");
        }
        return funcionario;
    }

    @Transactional
    public FuncionarioDTO buscaFuncionarioPorId(String id) {
        return new FuncionarioDTO(findById(id));
    }

    @Transactional
    public FuncionarioDTO salvaFuncionario(FuncionarioInputDTO funcionarioInputDTO) {
        Funcionario funcionario = toEntity(funcionarioInputDTO);
        buscarInserirDepartamentoEFilial(funcionarioInputDTO, funcionario);
        funcionario.setId(getUuid());
        return new FuncionarioDTO(funcionarioRepository.save(funcionario));
    }

    @Transactional
    public FuncionarioDTO atualizaFuncionario(String id, FuncionarioInputDTO funcionarioInputDTO) {
        Funcionario funcionario = findById(id);
        funcionario.setNome(funcionarioInputDTO.getNome());
        funcionario.setEmail(funcionarioInputDTO.getEmail());
        buscarInserirDepartamentoEFilial(funcionarioInputDTO, funcionario);
        Funcionario updatedFuncionario = funcionarioRepository.save(funcionario);
        return new FuncionarioDTO(updatedFuncionario);
    }

    private void buscarInserirDepartamentoEFilial(FuncionarioInputDTO funcionarioInputDTO, Funcionario funcionario) {
        if (funcionarioInputDTO.getDepartamento() != null) {
            Optional<Departamento> departamento = departamentoRepository.findById(funcionarioInputDTO.getDepartamento());
            departamento.ifPresent(funcionario::setDepartamento);
        } else {
            funcionario.setDepartamento(null);
        }

        if (funcionarioInputDTO.getFilial() != null) {
            Optional<Filial> filial = filialRepository.findById(funcionarioInputDTO.getFilial());
            filial.ifPresent(funcionario::setFilial);
        } else {
            funcionario.setFilial(null);
        }
    }

    @Transactional
    public void deletarFuncionario(String id) {
        findById(id);
        funcionarioRepository.deleteById(id);
    }

    public Funcionario toEntity(FuncionarioInputDTO funcionarioInputDTO) {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(funcionarioInputDTO.getId());
        funcionario.setNome(funcionarioInputDTO.getNome());
        funcionario.setEmail(funcionarioInputDTO.getEmail());
        return funcionario;
    }
}
