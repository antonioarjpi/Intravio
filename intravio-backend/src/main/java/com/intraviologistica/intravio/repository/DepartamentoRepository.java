package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    Departamento findByNomeIgnoreCase(String name);
}