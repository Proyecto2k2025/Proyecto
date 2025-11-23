package org.example.dao;

import org.hibernate.Session;

import java.util.List;

public interface GenericDao<T, ID > {

    ID create(Session session, T entity);
    T findById(Session session, ID id);
    T update(Session session, T entity);
    void delete(Session session, T entity);
    boolean deleteById(Session session, ID id);
    List<T> findAll(Session session);
}