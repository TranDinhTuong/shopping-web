package com.example.Shopping.Cart.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders; //don hang ho da dat
    private CartDto cart; //do hang cua ho
}