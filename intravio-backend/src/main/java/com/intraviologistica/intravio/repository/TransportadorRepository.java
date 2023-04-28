package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Transportador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportadorRepository extends JpaRepository<Transportador, String> {
}