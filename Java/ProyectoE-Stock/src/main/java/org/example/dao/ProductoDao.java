
package org.example.dao;

import org.example.domain.Producto;
import org.hibernate.Session;
import java.util.List;

public interface ProductoDao extends GenericDao<Producto, Long> {

    List<Producto> findByCategoria(Session session, String categoria);
}
