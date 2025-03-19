package org.unict.dieei.service;

import org.unict.dieei.persistence.AuthorizationDAO;

public class AuthorizationService {

    private AuthorizationDAO authorizationDAO;

    public AuthorizationService(AuthorizationDAO authorizationDAO) {
        this.authorizationDAO = authorizationDAO;
    }

    public boolean isAuthorized(String taxCode, String secretKey, int role) {
        try {
            return authorizationDAO.isAuthorized(taxCode, secretKey, role);
        } catch (Exception e) {
            System.out.println("Errore durante l'autorizzazione: " + e.getMessage());
            return false;
        }
    }

}
