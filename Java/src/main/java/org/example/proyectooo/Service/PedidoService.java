package org.example.proyectooo.Service;

import org.example.proyectooo.Domain.Pedido;
import org.example.proyectooo.Domain.PedidoItem;
import org.example.proyectooo.Domain.Variante;
import org.example.proyectooo.Repository.PedidoRepository;
import org.example.proyectooo.Repository.VarianteRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final VarianteRepository varianteRepository;
    private final ClienteService clienteService;

    public PedidoService(PedidoRepository pedidoRepository, VarianteRepository varianteRepository, ClienteService clienteService) {
        this.pedidoRepository = pedidoRepository;
        this.varianteRepository = varianteRepository;
        this.clienteService = clienteService;
    }

    @Transactional // CRÍTICO: Spring gestiona el Session, el commit y el rollback
    public Pedido crearFacturaYActualizarStock(Pedido pedido) {

        double totalFactura = 0.0;

        // Asegurar que el cliente existe antes de continuar (Lógica de negocio)
        if (pedido.getCliente() == null || pedido.getCliente().getId() == null || !clienteService.existsById(pedido.getCliente().getId())) {
            throw new IllegalArgumentException("La factura debe estar asociada a un cliente válido.");
        }

        // ITERAR, VALIDAR Y AJUSTAR STOCK
        for (PedidoItem item : pedido.getItems()) {

            // Cargar la variante desde la BD
            Variante varianteDB = varianteRepository.findById(item.getVariante().getId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Variante de producto no encontrada.")
                    );

            if (varianteDB.getStock() < item.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para Variante ID " + varianteDB.getId() +
                        ". Stock disponible: " + varianteDB.getStock());
            }

            // AJUSTE DE NEGOCIO
            varianteDB.setStock(varianteDB.getStock() - item.getCantidad());

            // CALCULAR TOTALES Y ASIGNAR ENLACES
            item.setPrecioUnitario(varianteDB.getProducto().getPrecio());
            item.setPedido(pedido);
            item.setVariante(varianteDB);

            totalFactura += item.getCantidad() * item.getPrecioUnitario();

            // Guardar la variante actualizada.
            varianteRepository.save(varianteDB);
        }

        // FINALIZAR Y GUARDAR PEDIDO
        pedido.setTotal(totalFactura);
        pedido.setFechaPedido(LocalDate.now());
        pedido.setEstado(Pedido.Estado.PENDIENTE);

        return pedidoRepository.save(pedido); // Guarda el nuevo pedido (con los items en cascada)
    }

    // Lectura: Spring gestiona la Session.
    public List<Pedido> obtenerPedidosPorFecha(LocalDate date) {
        return pedidoRepository.findByFechaPedido(date);
    }
}