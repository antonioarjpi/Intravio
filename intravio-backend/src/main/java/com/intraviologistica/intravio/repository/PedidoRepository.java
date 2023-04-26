package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p where p.romaneio.id = :id")
    Pedido findByRomaneioId(Long id);
}