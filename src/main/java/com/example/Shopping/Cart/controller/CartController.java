package com.example.Shopping.Cart.controller;

import com.example.Shopping.Cart.dto.CartDto;
import com.example.Shopping.Cart.dto.CartItemDto;
import com.example.Shopping.Cart.exceptions.ResourceNotFoundException;
import com.example.Shopping.Cart.model.Cart;
import com.example.Shopping.Cart.model.CartItem;
import com.example.Shopping.Cart.response.ApiResponse;
import com.example.Shopping.Cart.service.cart.ICartItemService;
import com.example.Shopping.Cart.service.cart.ICartService;
import com.example.Shopping.Cart.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
    private final ICartService cartService;
    private final ICartItemService cartItemService;
    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId){
        try {
            Cart cart = cartService.getCart(cartId);
            CartDto cartDto = cartService.convertToCartDto(cart);
            Set<CartItemDto> itemDtos = cart.getItems().stream().map(cartItemService::convertToItemDto).collect(Collectors.toSet());
            cartDto.setItems(itemDtos);

            return ResponseEntity.ok(new ApiResponse("Success", cartDto));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId){
        try {
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Clear cart success", cartId));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{cartId}/cart/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId){
        try {
            BigDecimal totalPrice  = cartService.getTotalPrice(cartId);
            return ResponseEntity.ok(new ApiResponse("total Price", totalPrice ));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
