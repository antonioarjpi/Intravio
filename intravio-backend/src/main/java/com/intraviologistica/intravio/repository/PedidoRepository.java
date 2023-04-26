package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}