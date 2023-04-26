package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Filial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {

    Filial findByNome(String name);
}