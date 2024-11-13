package com.example.Shopping.Cart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnore //khi bạn gửi yêu cầu tới API để lấy thông tin Category, thuộc tính products sẽ bị bỏ qua trong phản hồi JSON.
    private List<Product> products;

    public Category(String name) {
        this.name = name;
    }
}
