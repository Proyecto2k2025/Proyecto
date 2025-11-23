package org.example.dao;

import org.example.domain.Pedido;
import org.hibernate.Session;

import java.util.List;

public interface PedidoDao extends GenericDao<Pedido, Long> {

    List<Pedido> findByDate(Session session, java.time.LocalDate date);
}
