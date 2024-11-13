package com.example.Shopping.Cart.service.cart;

import com.example.Shopping.Cart.dto.CartDto;
import com.example.Shopping.Cart.dto.CartItemDto;
import com.example.Shopping.Cart.model.CartItem;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);

    CartItemDto convertToItemDto(CartItem item);
}
