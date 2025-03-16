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
        return productsDAO.getAllProducts();
    }

   public Products findById(int productId){
       return productsDAO.findById(productId);
   }

}
