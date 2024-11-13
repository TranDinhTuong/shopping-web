package com.example.Shopping.Cart.service.order;

import com.example.Shopping.Cart.dto.OrderDto;
import com.example.Shopping.Cart.enums.OrderStatus;
import com.example.Shopping.Cart.exceptions.ResourceNotFoundException;
import com.example.Shopping.Cart.model.Cart;
import com.example.Shopping.Cart.model.Order;
import com.example.Shopping.Cart.model.OrderItem;
import com.example.Shopping.Cart.model.Product;
import com.example.Shopping.Cart.repository.OrderRepository;
import com.example.Shopping.Cart.repository.ProductRepository;
import com.example.Shopping.Cart.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId); //lấy ra giỏ hàng của user
        Order order = createOrder(cart); //tạo đơn hàng từ giỏ hàng
        List<OrderItem> orderItemList = createOrderItems(order, cart); //tạo danh sách mục của từng đơn hàng
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(cart.getTotalAmount());
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId()); // khách hàng đã mua rồi thì giỏ hàng sẽ chuyển sang đơn hàng và item trong giỏ hàng đi
        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order(); //tạo đơn hàng mới
        order.setUser(cart.getUser()); // set đơn hàng cho user
        order.setOrderStatus(OrderStatus.PENDING); //set trạng thái
        order.setOrderDate(LocalDate.now()); //set ngày đặt hàng
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();  // lấy thông tin sản phẩm từ giỏ hàng
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return  new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice());
        }).toList();
    }


    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }


    @Override
    public List<OrderDto> getUserOrders(Long userId){ //lay ra cac don hang ma khach hang da order
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this :: convertToDto).toList();
    }

    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

}
