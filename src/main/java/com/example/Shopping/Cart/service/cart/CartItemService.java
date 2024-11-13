package com.example.Shopping.Cart.service.cart;

import com.example.Shopping.Cart.dto.CartItemDto;
import com.example.Shopping.Cart.exceptions.ResourceNotFoundException;
import com.example.Shopping.Cart.model.Cart;
import com.example.Shopping.Cart.model.CartItem;
import com.example.Shopping.Cart.model.Product;
import com.example.Shopping.Cart.repository.CartItemRepository;
import com.example.Shopping.Cart.repository.CartRepository;
import com.example.Shopping.Cart.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final ModelMapper modelMapper;
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId); //product da ton tai trong table product nhung chua cac no da co trong Cart (gio hang)
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId)) //lay ra cartItem cua product phia tren
                .findFirst().orElse(new CartItem());

        if(cartItem.getId() == null){
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice()); //price product
        }else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cartItem.setTotalPrice(); //cap nhat tong tien cua tung item trong gio hang
        cart.addItem(cartItem); //cap nhat tong tien cua gio hang
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);

        cart.removeItem(itemToRemove); //xoa trong cart thi cartItem cung tu dong bi xoa theo
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });

        //tinh lai tong tien sau khi cap nhat quantity
        BigDecimal totalAmount = cart.getItems().stream()
                .map(CartItem :: getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId)) //lay ra cartItem cua product phia tren
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }

    @Override
    public CartItemDto convertToItemDto(CartItem item){
        CartItemDto cartItemDto = modelMapper.map(item, CartItemDto.class);
        cartItemDto.setProduct(productService.convertDto(item.getProduct()));
        return cartItemDto;
    }
}
