package org.unict.dieei.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.unict.dieei.domain.User;

import java.util.List;

public class UserDAO {

    private EntityManager entityManager;

    public UserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User saveUser(User user) {
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        return user;
    }


    public User findByEmail(String email) {
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<User> findAllTechnicians() {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.role = 1", User.class).getResultList();
    }

    public List<User> findAllCustomers() {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.role = 2", User.class).getResultList();
    }

    public User findById(int userId) {
        return entityManager.find(User.class, userId);
    }

}