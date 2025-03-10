package org.unict.dieei.service;

import org.unict.dieei.dto.User;
import org.unict.dieei.persistence.UserDAO;

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
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setTaxCode(taxCode);
        user.setSecretKey(secretKey);

        userDAO.saveUser(user);
    }
}
