package org.example.product;

import java.util.List;

public class ProductService {
    public static ProductService productService;
    ProductRepository productRepository;

    private ProductService() {
        productRepository = new ProductRepository();
    }

    public static ProductService getInstance() {
        if (productService == null) {
            productService = new ProductService();
        }
        return productService;
    }

    public void addProduct(Product product) {
        productRepository.addProduct(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow();
    }

    public void removeProduct(long id) {
        productRepository.deleteProductById(id);
    }

    public void updateProduct(Product product) {
        productRepository.updateProductById(product);
    }
}
