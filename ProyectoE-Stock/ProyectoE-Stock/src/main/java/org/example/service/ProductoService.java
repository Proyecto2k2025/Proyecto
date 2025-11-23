package org.example.service;

import org.example.config.HibernateUtil;
import org.example.dao.ProductoDao;
import org.example.dao.ProductoDaoImpl;
import org.example.dao.UsuarioDao;
import org.example.domain.Producto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class ProductoService {

    private final SessionFactory ss;
    private final ProductoDao productoDAO;

    public ProductoService( ) {
        this.ss = HibernateUtil.getSessionFactory();
        this.productoDAO = new ProductoDaoImpl();
    }

    public List<Producto> obtenerInventario(String categoria) {
        try (Session session = ss.getCurrentSession()) {
            if (categoria != null && !categoria.trim().isEmpty()) {
                return productoDAO.findByCategoria(session, categoria);
            } else {
                return productoDAO.findAll(session); // Listar todo
            }
        }
    }

    public Producto registrarOActualizarProducto(Producto producto) {
        try (Session session = ss.getCurrentSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                //  Asegura que cada Variante sepa a qué Producto pertenece
                if (producto.getVariantes() != null) {
                    producto.getVariantes().forEach(v -> v.setProducto(producto));
                }

                productoDAO.update(session, producto); // Persistir o hacer merge

                tx.commit();
                return producto;
            } catch (RuntimeException e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
    }
    public Producto buscarPorId(Long id) {
        try (Session session = ss.getCurrentSession()) {
            return productoDAO.findById(session, id);
        }
    }
    public boolean eliminarProducto(Long id) {
        try (Session session = ss.getCurrentSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                boolean deleted = productoDAO.deleteById(session, id);
                tx.commit();
                return deleted;
            } catch (RuntimeException e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
    }




    
}
