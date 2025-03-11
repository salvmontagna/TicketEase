package org.unict.dieei.service;

import org.unict.dieei.domain.User;
import org.unict.dieei.persistence.UserDAO;

import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User login(String email, String password) {
        User user = userDAO.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void registerUser(String name, String email, String password, int role, String taxCode, String secretKey) {

        // Se l'utente è Admin (0) o Tecnico IT (1), deve essere autorizzato
        if ((role == 0 || role == 1) && !userDAO.isAuthorized(taxCode, secretKey, role)) {
            System.out.println("Registrazione negata: Codice fiscale o chiave segreta non validi per il ruolo selezionato.");
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setTaxCode(taxCode);
        user.setSecretKey(secretKey);

        userDAO.saveUser(user);
        System.out.println("Registrazione avvenuta con successo!");

    }

    public List<User> getAllTechnicians() {
        return userDAO.findAllTechnicians();
    }

    public List<User> getAllCustomers() {
        return userDAO.findAllCustomers();
    }

    public User getUserById(int id) {
        return userDAO.findById(id);
    }

}
