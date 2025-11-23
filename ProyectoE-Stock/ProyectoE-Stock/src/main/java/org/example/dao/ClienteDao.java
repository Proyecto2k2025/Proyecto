package org.example.dao;

import org.example.domain.Cliente;
import org.hibernate.Session;

public interface ClienteDao extends GenericDao<Cliente, Long> {

    Cliente findByNombre(Session session, String nombre);
}

