package com.example.Shopping.Cart.service.User;

import com.example.Shopping.Cart.dto.UserDto;
import com.example.Shopping.Cart.model.User;
import com.example.Shopping.Cart.request.CreateUserRequest;
import com.example.Shopping.Cart.request.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long id);
    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long id);

    UserDto convertUserDto(User user);

    User getAuthenticatedUser();
}
