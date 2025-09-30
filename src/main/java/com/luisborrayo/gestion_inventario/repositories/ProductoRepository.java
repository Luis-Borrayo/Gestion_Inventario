package com.luisborrayo.gestion_inventario.repositories;

import com.luisborrayo.gestion_inventario.models.Categoria;
import com.luisborrayo.gestion_inventario.models.Productos;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Productos producto) {
        if (producto.getId() == null) {
            em.persist(producto);
        } else {
            em.merge(producto);
        }
    }

    public Productos findById(Long id) {
        return em.find(Productos.class, id);
    }

    @Transactional
    public void delete(Long id) {
        Productos producto = em.find(Productos.class, id);
        if (producto != null) {
            em.remove(producto);
        }
    }

    @Transactional
    public void update(Productos producto) {

    }

    public List<Productos> findByStock(Integer stock) {
        return em.createQuery("SELECT p FROM Productos p WHERE p.stock = :stock",  Productos.class).setParameter("stock", stock).getResultList();
    }

    public List<Productos> findAll() {
        return em.createQuery("SELECT p FROM Productos p", Productos.class).getResultList();
    }

    public List<Productos> buscarconFiltro(String nombre, Categoria categoria, BigDecimal preciomin, BigDecimal preciomax,
                                           Productos.Estado estado, Integer stockmin, Integer stockmax, int page, int pageSize, String orderBy,
                                           boolean asc) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Productos> cq = cb.createQuery(Productos.class);
        Root<Productos> root = cq.from(Productos.class);
        List<Predicate> predicates = new ArrayList<>();

        if (nombre != null && !nombre.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
        }
        if (categoria != null) {
            predicates.add(cb.equal(root.get("categoria"), categoria));
        }
        if (preciomin != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("precio"), preciomin));
        }
        if (preciomax != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("precio"), preciomax));
        }
        if (estado != null) {
            predicates.add(cb.equal(root.get("estado"), estado));
        }
        if (stockmin != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("stock"), stockmin));
        }
        if (stockmax != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("stock"), stockmax));
        }

        if (sortBy != null && !sortBy.isEmpty()) {
            cq.orderBy(asc ? cb.asc(root.get(sortBy)) : cb.desc(root.get(sortBy)));
        }

        TypedQuery<Productos> query = em.createQuery(cq);
        query.setFirstResult(page * pageSize); // paginación
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

}
