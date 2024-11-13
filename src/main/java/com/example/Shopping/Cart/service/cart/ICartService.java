package com.example.Shopping.Cart.service.cart;

import com.example.Shopping.Cart.dto.CartDto;
import com.example.Shopping.Cart.model.Cart;
import com.example.Shopping.Cart.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);

    Cart initializeNewCart(User user);

    BigDecimal getTotalPrice(Long id);

    Cart getCartByUserId(Long userId);

    CartDto convertToCartDto(Cart cart);
}
