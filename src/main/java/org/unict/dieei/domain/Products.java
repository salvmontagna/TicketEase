package org.unict.dieei.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "product_name", unique = true, nullable = false, length = 30)
    private String productName;

    protected Products() {}

    public Products(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return id + ". " + productName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
