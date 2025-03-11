package org.unict.dieei.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "authorizations",
        uniqueConstraints = @UniqueConstraint(columnNames = "tax_code"))
public class Authorizations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "tax_code", length = 16, nullable = false, unique = true)
    private String taxCode;

    @Column(name = "secret_key", length = 255, nullable = false)
    private String secretKey;

    @Column(name = "role", nullable = false)
    private int role; // 0 = Admin, 1 = Tecnico IT

    protected Authorizations() {}

    public Authorizations(String taxCode, String secretKey, int role) {
        this.taxCode = taxCode;
        this.secretKey = secretKey;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTaxCode() { return taxCode; }
    public void setTaxCode(String taxCode) { this.taxCode = taxCode; }

    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }

    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }
}
