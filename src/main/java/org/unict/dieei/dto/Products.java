package org.unict.dieei.dto;

public class Products {
    private int id;
    private String productName;

    public Products(int id, String productName) {
        this.id = id;
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
