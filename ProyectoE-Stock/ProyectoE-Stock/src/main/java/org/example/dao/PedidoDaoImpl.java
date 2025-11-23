package org.example.dao;

import org.example.domain.Pedido;
import org.hibernate.Session;
import java.time.LocalDate;
import java.util.List;

public class PedidoDaoImpl extends GenericHibernateDao<Pedido, Long> implements PedidoDao {
    public PedidoDaoImpl() { super(Pedido.class); }

    @Override
    public List<Pedido> findByDate(Session session, LocalDate date) {
        // HQL para buscar pedidos por fecha específica
        return session.createQuery("FROM Pedido p WHERE p.fechaPedido = :date", Pedido.class)
                .setParameter("date", date)
                .getResultList();
    }
}