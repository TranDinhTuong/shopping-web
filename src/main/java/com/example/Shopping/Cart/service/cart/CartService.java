package com.example.Shopping.Cart.service.cart;

import com.example.Shopping.Cart.dto.CartDto;
import com.example.Shopping.Cart.exceptions.ResourceNotFoundException;
import com.example.Shopping.Cart.model.Cart;
import com.example.Shopping.Cart.model.User;
import com.example.Shopping.Cart.repository.CartItemRepository;
import com.example.Shopping.Cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);
    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.clearCart();
        cartRepository.deleteById(id);
    }

    //create new cart
    @Override
    public Cart initializeNewCart(User user){
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }
    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public CartDto convertToCartDto(Cart cart){
        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        return cartDto;
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
