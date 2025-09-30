package com.luisborrayo.gestion_inventario.repositories;

import com.luisborrayo.gestion_inventario.models.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class CategoriaRepository {
    @PersistenceContext
    private EntityManager em;

    // KPI: Total de categorías (LÍNEA AZUL)
    public Long getTotalCategorias() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Categoria> root = query.from(Categoria.class);
        query.select(cb.count(root));
        return em.createQuery(query).getSingleResult();
    }
}