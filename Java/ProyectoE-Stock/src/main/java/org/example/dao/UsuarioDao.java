package org.example.dao;

import org.example.domain.Usuario;
import org.hibernate.Session;

public interface UsuarioDao extends GenericDao<Usuario, Long> {
    Usuario findByUsername(Session session, String username);
}