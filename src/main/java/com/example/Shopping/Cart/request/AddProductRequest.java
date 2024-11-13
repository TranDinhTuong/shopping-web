package com.example.Shopping.Cart.request;

import com.example.Shopping.Cart.model.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;

@Data //da co getter and setter
public class AddProductRequest {
    private Long id;
    private String name;
    private String brand;

    private BigDecimal price; //so thap phan
    private int inventory; //so luong
    private String description;
    private Category category;
}
