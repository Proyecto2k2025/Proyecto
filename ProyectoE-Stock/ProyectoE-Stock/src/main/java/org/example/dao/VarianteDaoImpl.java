package org.example.dao;

import org.example.domain.Variante;

public class VarianteDaoImpl extends GenericHibernateDao<Variante, Long> implements VarianteDao {
    public VarianteDaoImpl() { super(Variante.class); }
}