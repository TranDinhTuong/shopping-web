package com.example.Shopping.Cart.request;

import com.example.Shopping.Cart.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price; //so thap phan
    private int inventory; //so luong
    private String description;
    private Category category;
}
