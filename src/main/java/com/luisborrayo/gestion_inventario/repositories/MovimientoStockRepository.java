package com.luisborrayo.gestion_inventario.repositories;

import com.luisborrayo.gestion_inventario.models.MovimientoStock;
import com.luisborrayo.gestion_inventario.models.Productos;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository para Movimientos de Stock
 */
public class MovimientoStockRepository {

    @PersistenceContext
    private EntityManager em;

    // ==================== LÍNEA AZUL: Registrar Entrada y Salida ====================
    @Transactional
    public void save(MovimientoStock movimiento) {
        em.persist(movimiento);
    }

    public long countByFechaDesde(LocalDate fecha) {
        return em.createQuery(
                        "SELECT COUNT(m) FROM MovimientoStock m WHERE m.fecha >= :fecha", Long.class)
                .setParameter("fecha", fecha)
                .getSingleResult();
    }

    public LocalDate findMaxFecha() {
        return em.createQuery(
                        "SELECT MAX(m.fecha) FROM MovimientoStock m", LocalDate.class)
                .getSingleResult();
    }

    /**
     * LÍNEA AZUL: Registrar Entrada
     * Registra entrada de stock (fecha, cantidad, motivo, producto)
     * Actualiza el stockActual del producto
     */
    @Transactional
    public MovimientoStock registrarEntrada(Long productoId, Integer cantidad, String motivo) {
        // Crear movimiento
        MovimientoStock movimiento = new MovimientoStock();
        movimiento.setProductoId(productoId);
        movimiento.setTipo("ENTRADA");
        movimiento.setCantidad(cantidad);
        movimiento.setMotivo(motivo);
        movimiento.setFecha(LocalDate.now());
        em.persist(movimiento);

        // LÍNEA VERDE: Actualizar stockActual del producto
        Productos producto = em.find(Productos.class, productoId);
        if (producto != null) {
            producto.setStock(producto.getStock() + cantidad);
            em.merge(producto);
        }

        return movimiento;
    }

    /**
     * LÍNEA AZUL: Registrar Salida
     * Registra salida de stock (fecha, cantidad, motivo, producto)
     * Actualiza el stockActual del producto
     * LÍNEA VERDE: Valida que la salida no deje stock negativo
     */
    @Transactional
    public MovimientoStock registrarSalida(Long productoId, Integer cantidad, String motivo) {
        Productos producto = em.find(Productos.class, productoId);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado");
        }

        if (producto.getEstado() == Productos.Estado.Inactivo) {
            throw new IllegalStateException("El producto está inactivo y no admite salidas.");
        }

        if (producto.getStock() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente. Stock actual: " +
                    producto.getStock() + ", cantidad solicitada: " + cantidad);
        }

        // Crear y guardar movimiento
        MovimientoStock movimiento = new MovimientoStock();
        movimiento.setProductoId(productoId);
        movimiento.setTipo("SALIDA");
        movimiento.setCantidad(cantidad);
        movimiento.setMotivo(motivo);
        movimiento.setFecha(LocalDate.now());
        em.persist(movimiento);

        // Actualizar stock
        producto.setStock(producto.getStock() - cantidad);
        em.merge(producto);

        return movimiento;
    }

    // ==================== LÍNEA AZUL: Listado con filtros ====================

    /**
     * LÍNEA AZUL: Listado con filtros por producto, tipo y rango de fechas
     */
    public List<MovimientoStock> listarConFiltros(
            Long productoId,
            String tipo,
            LocalDate fechaDesde,
            LocalDate fechaHasta) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MovimientoStock> query = cb.createQuery(MovimientoStock.class);
        Root<MovimientoStock> root = query.from(MovimientoStock.class);

        List<Predicate> predicates = new ArrayList<>();

        // Filtro por producto
        if (productoId != null) {
            predicates.add(cb.equal(root.get("productoId"), productoId));
        }

        // Filtro por tipo (ENTRADA/SALIDA)
        if (tipo != null && !tipo.isEmpty()) {
            predicates.add(cb.equal(root.get("tipo"), tipo));
        }

        // Filtro por rango de fechas
        if (fechaDesde != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("fecha"), fechaDesde));
        }
        if (fechaHasta != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("fecha"), fechaHasta));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("fecha")));

        return em.createQuery(query).getResultList();
    }

    // ==================== LÍNEA ROSA: Métodos adicionales ====================

    /**
     * LÍNEA ROSA: Obtener todos los movimientos
     */
    public List<MovimientoStock> findAll() {
        return em.createQuery("SELECT m FROM MovimientoStock m ORDER BY m.fecha DESC",
                MovimientoStock.class).getResultList();
    }

    /**
     * LÍNEA ROSA: Buscar movimiento por ID
     */
    public MovimientoStock findById(Long id) {
        return em.find(MovimientoStock.class, id);
    }

    /**
     * LÍNEA ROSA: Obtener últimos N movimientos
     */
    public List<MovimientoStock> getUltimosMovimientos(int cantidad) {
        return em.createQuery(
                        "SELECT m FROM MovimientoStock m ORDER BY m.fecha DESC",
                        MovimientoStock.class)
                .setMaxResults(cantidad)
                .getResultList();
    }

    /**
     * LÍNEA ROSA: Contar movimientos por producto
     */
    public Long contarMovimientosPorProducto(Long productoId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<MovimientoStock> root = query.from(MovimientoStock.class);

        query.select(cb.count(root));
        query.where(cb.equal(root.get("productoId"), productoId));

        return em.createQuery(query).getSingleResult();
    }
}