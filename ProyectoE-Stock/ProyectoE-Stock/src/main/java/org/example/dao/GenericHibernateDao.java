package org.example.dao;

import org.hibernate.Session;

import java.util.List;

public class GenericHibernateDao<T, ID> implements GenericDao<T, ID> {

    private final Class<T> entityClass;

    public GenericHibernateDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public ID create(Session session, T entity) {
        session.persist(entity);
        session.flush();
        return (ID) session.getIdentifier(entity);
    }

    @Override
    public T update(Session session, T entity) {
        return (T) session.merge(entity);
    }

    @Override
    public void delete(Session session, T entity) {
        session.remove(entity);
    }

    @Override
    public T findById(Session session, ID id) {
        return session.find(entityClass, id);
    }

    @Override
    public boolean deleteById(Session session, ID id) {
        T found = findById(session, id);
        if (found != null) {
            delete(session, found);
            return true;
        }
        return false;
    }

    @Override
    public List<T> findAll(Session session) {
        return List.of();
    }
}
