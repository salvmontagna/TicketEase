package org.unict.dieei.persistence;

import jakarta.persistence.EntityManager;
import org.unict.dieei.domain.User;

import java.util.List;

public class UserDAO {

    private EntityManager entityManager;

    public UserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void saveUser(User user) {
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
    }

    public User findByEmail(String email) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public List<User> findAllTechnicians() {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.role = 1", User.class).getResultList();
    }

    public List<User> findAllCustomers() {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.role = 2", User.class).getResultList();
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

    public User findById(int userId) {
        return entityManager.find(User.class, userId);
    }

}