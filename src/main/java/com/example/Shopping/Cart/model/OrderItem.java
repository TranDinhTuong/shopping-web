package com.example.Shopping.Cart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal price;


    @ManyToOne
    @JoinColumn(name = "order_id") //them cot khoa ngoai
    private Order order;

    //OrderItem cũng có mối quan hệ "Nhiều-1" với Product, tức là nhiều mục hàng có thể cùng tham chiếu đến một sản phẩm.
    @ManyToOne
    @JoinColumn(name = "product_id") //them cot khoa ngoai
    private Product product;

    public OrderItem(Order order, Product product, int quantity, BigDecimal price) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
