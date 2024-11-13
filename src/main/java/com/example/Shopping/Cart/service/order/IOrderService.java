package com.example.Shopping.Cart.service.order;

import com.example.Shopping.Cart.dto.OrderDto;
import com.example.Shopping.Cart.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
