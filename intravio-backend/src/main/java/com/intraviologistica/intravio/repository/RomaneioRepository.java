package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Romaneio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RomaneioRepository extends JpaRepository<Romaneio, String> {

    @Query("SELECT COALESCE(MAX(r.numeroRomaneio), 0) FROM Romaneio r")
    Integer getMaxNumeroRomaneio();

    @Query("SELECT r FROM Romaneio r WHERE r.dataCriacao BETWEEN :min AND :max ORDER BY r.numeroRomaneio desc")
    List<Romaneio> findAll(LocalDateTime min, LocalDateTime max);
}