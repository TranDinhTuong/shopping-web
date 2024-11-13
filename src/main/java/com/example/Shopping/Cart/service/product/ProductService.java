package com.example.Shopping.Cart.service.product;

import com.example.Shopping.Cart.dto.ImageDto;
import com.example.Shopping.Cart.dto.ProductDto;
import com.example.Shopping.Cart.exceptions.AlreadyExistsException;
import com.example.Shopping.Cart.exceptions.ProductNotFoundException;
import com.example.Shopping.Cart.model.Category;
import com.example.Shopping.Cart.model.Image;
import com.example.Shopping.Cart.model.Product;
import com.example.Shopping.Cart.repository.CategoryRepository;
import com.example.Shopping.Cart.repository.ImageRepository;
import com.example.Shopping.Cart.repository.ProductRepository;
import com.example.Shopping.Cart.request.AddProductRequest;
import com.example.Shopping.Cart.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.AlreadyBoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor //da bao gom autowired
public class ProductService implements IProductService{

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;

    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest request) {
        if(productExists(request.getName(), request.getBrand())){
            throw new AlreadyExistsException(request.getBrand() + " " + request.getName() + " already exists, you may update this product instead!");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() ->{
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });

        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private boolean productExists(String name, String brand){
        return productRepository.existsByNameAndBrand(name, brand);
    }
    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not Found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,
                () -> {throw new ProductNotFoundException("Product not Found!");} ); //ton tai product co id thi xoa
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository :: save)
                .orElseThrow(() -> {throw new ProductNotFoundException("Product not Found!");});
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getAllProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getAllProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getAllProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getAllProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertDto).toList();
    }
    @Override
    public ProductDto convertDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class); //convert product to productDto
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream() //tra ve tuan tu tung doi tuong trong images
                .map(image-> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
