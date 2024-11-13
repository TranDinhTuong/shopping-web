package com.example.Shopping.Cart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity; //so luong
    private BigDecimal unitPrice; //don gia
    private BigDecimal totalPrice;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Product product;
    //@JoinColumn(name = "cart_id"):Đặt tên cột (cart_id) trong bảng CartItem để lưu trữ khóa ngoại (foreign key) liên kết với bảng Cart.
    // Khóa ngoại này liên kết mỗi CartItem với một Cart tương ứng.

    //orphanRemoval = true:Khi một CartItem không còn liên kết với bất kỳ Cart nào (bị "mồ côi" – orphaned), nó sẽ tự động bị xóa khỏi cơ sở dữ liệu.
    // Điều này đảm bảo rằng khi bạn xóa một mục khỏi giỏ hàng (hoặc xóa giỏ hàng), các mục liên quan sẽ bị xóa theo.
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public void setTotalPrice() {
        this.totalPrice = this.unitPrice.multiply(new BigDecimal(quantity));
    }
}
