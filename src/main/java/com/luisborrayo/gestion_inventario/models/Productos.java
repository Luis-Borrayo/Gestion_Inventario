package com.luisborrayo.gestion_inventario.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "productos")
@NamedQuery(
        name = "productos.stock",
        query = "SELECT p FROM Productos p WHERE p.stock = :stock"
)
public class Productos {
        public enum Estado {
        Activo,
        Inactivo
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 120, unique = true, nullable = false)
    private String nombre;

    @OneToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Column(nullable = false)
    private BigDecimal precio;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaDeIngreso;

    @Column(nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    public Productos() {}

    public Productos(String nombre, Categoria categoria, BigDecimal precio, LocalDateTime fechaDeIngreso, Integer stock, Estado estado) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.fechaDeIngreso = fechaDeIngreso;
        this.stock = stock;
        this.estado = estado;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Categoria getCategoria() {
        return categoria;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    public BigDecimal getPrecio() {
        return precio;
    }
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
    public LocalDateTime getFechaDeIngreso() {
        return fechaDeIngreso;
    }
    public void setFechaDeIngreso(LocalDateTime fechaDeIngreso) {
        this.fechaDeIngreso = fechaDeIngreso;
    }
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public Estado getEstado() {
        return estado;
    }
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}

