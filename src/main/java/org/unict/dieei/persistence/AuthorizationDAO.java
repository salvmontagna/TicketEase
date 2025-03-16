package org.unict.dieei.persistence;

import jakarta.persistence.EntityManager;

public class AuthorizationDAO {

    private EntityManager entityManager;

    public AuthorizationDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean isAuthorized(String taxCode, String secretKey, int role) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(a) FROM Authorizations a WHERE LOWER(a.taxCode) = LOWER(:taxCode) " +
                                "AND LOWER(a.secretKey) = LOWER(:secretKey) AND a.role = :role", Long.class)
                .setParameter("taxCode", taxCode)
                .setParameter("secretKey", secretKey)
                .setParameter("role", role)
                .getSingleResult();
        return count > 0;
    }

}
