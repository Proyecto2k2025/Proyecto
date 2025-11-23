package org.example.dao;

import org.example.domain.Producto;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;


public class ProductoDaoImpl extends GenericHibernateDao<Producto, Long> implements ProductoDao {

    public ProductoDaoImpl() {
        super(Producto.class);
    }

    @Override
    public List<Producto> findByCategoria(Session session, String categoria){
        String hql = "FROM Producto p WHERE p.categoria = :categoria";

        Query<Producto> query = session.createQuery(hql, Producto.class);
        query.setParameter("categoria",categoria);

        return query.getResultList();
    }
}