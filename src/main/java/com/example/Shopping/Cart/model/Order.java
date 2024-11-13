package com.example.Shopping.Cart.model;

import com.example.Shopping.Cart.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    //Mỗi Order có thể chứa nhiều sản phẩm khác nhau được liệt kê dưới dạng các mục hàng
    //mappedBy = "order" Trường order trong thực thể OrderItem chính là khóa ngoại (foreign key) tham chiếu đến Order.

    //Thuộc tính cascade = CascadeType.ALL chỉ ra rằng tất cả các thao tác (persist, merge, remove, refresh, detach) sẽ được "cascade" từ Order sang các OrderItem liên quan.
    //Ví dụ: Khi bạn lưu (persist) một Order, tất cả các OrderItem liên quan cũng sẽ được lưu tự động.
    // Tương tự, nếu bạn xóa (remove) Order, tất cả các OrderItem của nó cũng sẽ bị xóa tự động.

    //Thuộc tính orphanRemoval = true đảm bảo rằng nếu một OrderItem bị xóa khỏi tập hợp orderItems của Order (tức là nó không còn thuộc về Order nữa), thực thể OrderItem đó sẽ tự động bị xóa khỏi cơ sở dữ liệu.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
