package com.luisborrayo.gestion_inventario.Dashboard;

import com.luisborrayo.gestion_inventario.repositories.CategoriaRepository;
import com.luisborrayo.gestion_inventario.repositories.MovimientoStockRepository;
import com.luisborrayo.gestion_inventario.repositories.ProductoRepository;
import jakarta.inject.Inject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DashboardService {
    @Inject
    private ProductoRepository productoRepository;

    @Inject
    private CategoriaRepository categoriaRepository;

    @Inject
    private MovimientoStockRepository movimientoStockRepository;

    //Punto 1 completado :D
    public long getTotalProductos() {
        return productoRepository.count();
    }

    public long getProductosStockBajo(int umbral) {
        return productoRepository.countStockBajo(umbral);
    }

    public long getProductosInactivos() {
        return productoRepository.countInactivos();
    }

    public long getTotalCategorias() {
        return categoriaRepository.count();
    }

    // 🔹 Movimientos registrados esta semana
    public long getMovimientosSemana() {
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDateTime inicioSemana = monday.atStartOfDay();
        return movimientoStockRepository.countByFechaDesde(inicioSemana);
    }

    // 🔹 Última actualización de inventario
    public LocalDateTime getUltimaActualizacionInventario() {
        return movimientoStockRepository.findMaxFecha();
    }
}
