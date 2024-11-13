package com.example.Shopping.Cart.repository;

import com.example.Shopping.Cart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email); //kiem tra email da ton tai chua
    User findByEmail(String email);
}
