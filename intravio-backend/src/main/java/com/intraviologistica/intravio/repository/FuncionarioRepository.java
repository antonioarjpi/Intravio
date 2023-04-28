package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {

    Funcionario findByEmail(String email);
}