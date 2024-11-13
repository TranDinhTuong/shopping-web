package com.example.Shopping.Cart.service.product;

import com.example.Shopping.Cart.dto.ProductDto;
import com.example.Shopping.Cart.model.Product;
import com.example.Shopping.Cart.request.AddProductRequest;
import com.example.Shopping.Cart.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest request);

    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);

    List<Product> getAllProducts();
    List<Product> getAllProductsByCategory(String category);
    List<Product> getAllProductsByBrand(String brand);
    List<Product> getAllProductsByCategoryAndBrand(String category, String brand);

    List<Product> getAllProductsByName(String name);
    List<Product> getAllProductsByBrandAndName(String brand, String name);

    Long countProductsByBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertDto(Product product);
}
