    package com.example.Shopping.Cart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;

    @Lob //cho phep luu anh duoi database
    private Blob image; //dinh dang file anh

    private String downloadUrl;

    @ManyToOne //co nhieu hinh anh cua 1 product
    @JoinColumn(name = "product_id") //khoa ngoai
    private Product product;
}
