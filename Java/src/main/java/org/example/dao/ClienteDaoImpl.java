package org.example.dao;


import org.example.domain.Cliente;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class ClienteDaoImpl extends GenericHibernateDao<Cliente, Long> implements ClienteDao {
    
    public ClienteDaoImpl() {
        super(Cliente.class);
    }

    @Override
    public Cliente findByNombre(Session session, String nombre) {
        Query<Cliente> query = session.createQuery(
                "FROM Cliente c WHERE c.nombre = :nombre", Cliente.class
        );
        query.setParameter("nombre", nombre);
        return query.uniqueResult();
    }
}
