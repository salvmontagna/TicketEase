package org.unict.dieei.service;

import org.unict.dieei.domain.Products;
import org.unict.dieei.persistence.ProductsDAO;

import java.util.List;

public class ProductsService {

    private ProductsDAO productsDAO;

    public ProductsService(ProductsDAO productsDAO) {
        this.productsDAO = productsDAO;
    }

    public List<Products> getAllProducts(){
        try {
            return productsDAO.getAllProducts();
        } catch (Exception e) {
            System.out.println("Errore durante il recupero dei prodotti: " + e.getMessage());
            return null;
        }
    }

   public Products findProductById(int productId){
        try{
            return productsDAO.findById(productId);
        } catch (Exception e) {
            System.out.println("Errore durante il recupero del prodotto: " + e.getMessage());
            return null;
        }
   }

}
