package org.example.dao;

import org.example.domain.Usuario;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class UsuarioDaoImpl extends GenericHibernateDao<Usuario,Long> implements UsuarioDao {

    public UsuarioDaoImpl() {
        super(Usuario.class);
    }
    @Override
    public Usuario findByUsername(Session session, String username) {
        Query<Usuario> query = session.createQuery(
                "FROM Usuario u WHERE u.nombre = :username", Usuario.class
        );
        query.setParameter("username", username);

        return query.uniqueResult();
    }
}
