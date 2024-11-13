package com.example.Shopping.Cart.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO;

    //cascade = CascadeType.ALL: Khi thực hiện các hành động (như persist, merge, remove) trên một đối tượng CartItem,
    //các hành động tương tự cũng sẽ được áp dụng cho đối tượng Cart mà nó tham chiếu.
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true) //cart bi xoa thi all cartItem cung bi xoa
    private Set<CartItem> items = new HashSet<>();


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void addItem(CartItem item) {
        this.items.add(item);
        item.setCart(this);
        updateTotalAmount();
    }

    public void removeItem(CartItem item) {
        this.items.remove(item);
        item.setCart(null);
        updateTotalAmount();
    }

    //.reduce(BigDecimal.ZERO, BigDecimal::add)
    //Dùng reduce() để cộng dồn các giá trị của tất cả các mặt hàng.
    // Phương thức này bắt đầu với giá trị ban đầu là BigDecimal.ZERO (tức là 0) và cộng tất cả các giá trị bằng cách sử dụng BigDecimal::add.
    private void updateTotalAmount() {
        this.totalAmount = items.stream().map(item -> {
            BigDecimal unitPrice = item.getUnitPrice();
            if (unitPrice == null) {
                return  BigDecimal.ZERO;
            }
            return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void clearCart(){
        this.items.clear();
        updateTotalAmount();
    }



}
