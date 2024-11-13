package com.example.Shopping.Cart.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank //ko đc phép để trống của validation
    private String email;
    @NotBlank
    private String password;
}
