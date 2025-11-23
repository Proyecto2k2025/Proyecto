package org.example.service;


import org.example.config.HibernateUtil;
import org.example.dao.ClienteDao;
import org.example.dao.ClienteDaoImpl;
import org.example.domain.Cliente;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class ClienteService {

    private final ClienteDao clienteDao;
    private SessionFactory ss;

    public ClienteService() {
        this.clienteDao = new ClienteDaoImpl();
        this.ss = HibernateUtil.getSessionFactory();
    }

    /**
     * Guarda un nuevo cliente o actualiza uno existente.
     * Es utilizado por el endpoint POST /api/v1/clientes.
     */
    public Cliente save(Cliente cliente) {
        try (Session session = ss.getCurrentSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                if (cliente.getNombre() == null || cliente.getNombre().isEmpty()) {
                    throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
                }

                Cliente clienteGuardado = clienteDao.update(session, cliente);
                tx.commit();
                return clienteGuardado;
            } catch (RuntimeException e) {
                if (tx != null) tx.rollback();
                throw new RuntimeException("Error al guardar el cliente.", e);
            }
        }
    }

    /**
     * Busca un cliente por ID.
     * Utilizado para validación en otros servicios o por el controlador.
     */
    public Cliente findById(Long id) {
        try (Session session = ss.getCurrentSession()) {
            // No se requiere transacción para una simple lectura
            return clienteDao.findById(session, id);
        }
    }

    // Metodo auxiliar para el PedidoService: verificar existencia del ID
    public boolean existsById(Long id) {
        return findById(id) != null;
    }

    // Metodo auxiliar para el listado de clientes
    public List<Cliente> findAll() {
        try (Session session = ss.getCurrentSession()) {
            return clienteDao.findAll(session);
        }
    }
}