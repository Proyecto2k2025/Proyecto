package org.example.service;

import org.example.config.HibernateUtil;
import org.example.dao.*;
import org.example.dao.VarianteDaoImpl;
import org.example.domain.Pedido;
import org.example.domain.PedidoItem;
import org.example.domain.Variante;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class PedidoService {

    private final PedidoDao pedidoDao;
    private SessionFactory ss;
    private final VarianteDao varianteDao;
    private final ClienteService clienteService;

    public PedidoService( ) {
        this.ss = HibernateUtil.getSessionFactory();
       this.pedidoDao = new PedidoDaoImpl();
       this.varianteDao = new VarianteDaoImpl();
       this.clienteService = new ClienteService();

    }

    public Pedido crearFacturaYActualizarStock(Pedido pedido) {

        try (Session session = ss.getCurrentSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                double totalFactura = 0.0;

                // Asegurar que el cliente existe antes de continuar
                if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
                    throw new IllegalArgumentException("La factura debe estar asociada a un cliente válido.");
                }

                // ITERAR, VALIDAR Y AJUSTAR STOCK
                for (PedidoItem item : pedido.getItems()) {
                    // Cargar la variante desde la BD
                    Variante varianteDB = varianteDao.findById(session, item.getVariante().getId());

                    if (varianteDB == null) {
                        throw new IllegalArgumentException("Variante de producto no encontrada.");
                    }

                    if (varianteDB.getStock() < item.getCantidad()) {
                        throw new IllegalStateException("Stock insuficiente para Variante ID " + varianteDB.getId() +
                                ". Stock disponible: " + varianteDB.getStock());
                    }

                    //  AJUSTE DE NEGOCIO
                    varianteDB.setStock(varianteDB.getStock() - item.getCantidad());

                    //  CALCULAR TOTALES Y PERSISTENCIA DE ITEMS
                    item.setPrecioUnitario(varianteDB.getProducto().getPrecio()); // Usar el precio del Producto asociado
                    item.setPedido(pedido);
                    item.setVariante(varianteDB); // Asegurar el enlace

                    totalFactura += item.getCantidad() * item.getPrecioUnitario();

                    //  Actualizar la variante en la BD
                    varianteDao.update(session, varianteDB);
                }

                //  FINALIZAR Y GUARDAR PEDIDO
                pedido.setTotal(totalFactura);
                pedido.setFechaPedido(LocalDate.now());
                pedido.setEstado(Pedido.Estado.PENDIENTE); // Estado inicial

                pedidoDao.create(session, pedido);

                tx.commit();
                return pedido;

            } catch (RuntimeException e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
    }
    public List<Pedido> obtenerPedidosPorFecha(LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return pedidoDao.findByDate(session, date);
        }
    }

}
