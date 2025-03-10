package org.unict.dieei.persistence;

import jakarta.persistence.EntityManager;
import org.unict.dieei.dto.Products;

import java.util.List;

public class ProductsDAO {

    private EntityManager entityManager;

    public ProductsDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Products findById(int id) {
        return entityManager.find(Products.class, id);
    }

    public List<Products> getAllProducts() {
        return entityManager.createQuery("SELECT p FROM Products p", Products.class)
                .getResultList();
    }
}
