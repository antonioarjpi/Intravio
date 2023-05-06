package com.intraviologistica.intravio.repository;

import com.intraviologistica.intravio.model.HistoricoPedido;
import com.intraviologistica.intravio.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, String> {

    @Query("SELECT p FROM Pedido p where p.romaneio.id = :id")
    List<Pedido> findByRomaneioId(String id);

    @Query("SELECT h FROM HistoricoPedido h, Pedido p WHERE p.id = h.pedido.id AND p.codigoRastreio = :codigo")
    List<HistoricoPedido> findByCodigoRastreio(String codigo);

    Optional<Pedido> findByNumeroPedido(Integer numeroPedido);

    @Query("SELECT COALESCE(MAX(p.numeroPedido), 0) FROM Pedido p")
    Integer getMaxNumeroPedido();

    @Modifying
    @Query("DELETE FROM Item obj WHERE obj.id.pedido.id = :pedidoId")
    void deleteItemPedidoByPedidoId(String pedidoId);

    @Query(value = "SELECT * FROM Pedido p WHERE p.status_pedido = :status ORDER BY p.numero_pedido asc", nativeQuery = true)
    List<Pedido> findAllByStatus(Integer status);
}