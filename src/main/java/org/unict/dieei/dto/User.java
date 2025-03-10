package org.unict.dieei.dto;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;
    private String password;
    private int role;  // 0 = Admin, 1 = Tecnico IT, 2 = Cliente

    @Column(name = "tax_code", nullable = true)
    private String taxCode;

    @Column(name = "secret_key", nullable = true)
    private String secretKey;

    public User() { }

    // Costruttore con parametri per inizializzazione
    public User(String name, String email, String password, int role, String taxCode, String secretKey) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.taxCode = taxCode;
        this.secretKey = secretKey;
    }

    // Getters e Setters
    public int getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }

    public String getTaxCode() { return taxCode; }
    public void setTaxCode(String taxCode) { this.taxCode = taxCode; }

    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
}