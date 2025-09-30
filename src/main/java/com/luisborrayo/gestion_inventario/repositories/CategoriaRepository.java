package com.luisborrayo.gestion_inventario.repositories;

import com.luisborrayo.gestion_inventario.models.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

public class CategoriaRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Categoria categoria) {
        if (categoria.getId() == null) {
            em.persist(categoria);
        } else{
            em.merge(categoria);
        }
    }

    public Categoria findById(Long id) {
        return em.find(Categoria.class, id);
    }

    public List<Categoria> findAll() {
        return em.createQuery("SELECT c FROM Categoria c", Categoria.class).getResultList();
    }

    @Transactional
    public void delete(Long id) {
        Categoria c = em.find(Categoria.class, id);
        if (c != null) {
            em.remove(c);
        }
    }
}
