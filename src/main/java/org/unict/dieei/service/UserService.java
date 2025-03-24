package org.unict.dieei.service;

import org.unict.dieei.domain.User;
import org.unict.dieei.persistence.UserDAO;

import java.util.List;

public class UserService {

    private UserDAO userDAO;
    private AuthorizationService authorizationService;

    public UserService(UserDAO userDAO, AuthorizationService authorizationService) {
        this.userDAO = userDAO;
        this.authorizationService = authorizationService;
    }

    public User loginUser(String email, String password) {
        User user = findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("\nLogin riuscito! Benvenuto, " + user.getName());
            return user;
        }

        System.out.println("Credenziali errate.");
        return null;
    }

    public User registerUser(String name, String email, String password, int role, String taxCode, String secretKey) {

        // Se l'utente è Admin (0) o Tecnico IT (1), deve essere autorizzato
        if ((role == 0 || role == 1) && !authorizationService.isAuthorized(taxCode, secretKey, role)) {
            System.out.println("Registrazione negata: Codice fiscale o chiave segreta non validi per il ruolo selezionato.");
            return null;
        }

        User user = findByEmail(email);
        if (user != null) {
            System.out.println("Registrazione negata: Email già in uso.");
            return null;
        }

        user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setTaxCode(taxCode);
        user.setSecretKey(secretKey);

        try {
            user = userDAO.saveUser(user);
        } catch (Exception e) {
            System.out.println("Errore durante la registrazione: " + e.getMessage());
            return null;
        }

        if (user != null) {
            System.out.println("Registrazione completata con successo.");
        } else {
            System.out.println("Errore durante la registrazione.");
        }

        return user;

    }

    public List<User> findAllTechnicians() {
        try {
            return userDAO.findAllTechnicians();
        } catch (Exception e) {
            System.out.println("Errore durante il recupero dei tecnici: " + e.getMessage());
            return null;
        }
    }

    public List<User> findAllCustomers() {
        try {
            return userDAO.findAllCustomers();
        } catch (Exception e) {
            System.out.println("Errore durante il recupero dei clienti: " + e.getMessage());
            return null;
        }
    }

    public User findById(int id) {
        try {
            return userDAO.findById(id);
        } catch (Exception e) {
            System.out.println("Errore durante il recupero dell'utente: " + e.getMessage());
            return null;
        }
    }

    public User findByEmail(String email) {
        try {
            return userDAO.findByEmail(email);
        } catch (Exception e) {
            System.out.println("Errore durante il recupero dell'utente: " + e.getMessage());
            return null;
        }
    }

}