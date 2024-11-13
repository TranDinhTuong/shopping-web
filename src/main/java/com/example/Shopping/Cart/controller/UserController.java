package com.example.Shopping.Cart.controller;

import com.example.Shopping.Cart.dto.UserDto;
import com.example.Shopping.Cart.exceptions.AlreadyExistsException;
import com.example.Shopping.Cart.exceptions.ResourceNotFoundException;
import com.example.Shopping.Cart.model.User;
import com.example.Shopping.Cart.request.CreateUserRequest;
import com.example.Shopping.Cart.request.UpdateUserRequest;
import com.example.Shopping.Cart.response.ApiResponse;
import com.example.Shopping.Cart.service.User.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        try{
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertUserDto(user);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request){
        try{
            User user = userService.createUser(request);
            UserDto userDto = userService.convertUserDto(user);
            return ResponseEntity.ok(new ApiResponse("Create User Success!", userDto));
        }catch (AlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest request){
        try{
            User user = userService.updateUser(request, userId);
            UserDto userDto = userService.convertUserDto(user);
            return ResponseEntity.ok(new ApiResponse("Update User Success!", userDto));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        try{
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Delete User Success!", null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
