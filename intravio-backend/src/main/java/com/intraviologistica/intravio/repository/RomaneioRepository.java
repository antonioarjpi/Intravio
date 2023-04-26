package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Romaneio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RomaneioRepository extends JpaRepository<Romaneio, Long> {
}