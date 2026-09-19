package com.example.service;

import com.example.dto.request.UserCreateRequest;
import com.example.dto.request.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);

    void sendVerificationCode(String email);

    void verifyEmail(String email, String otp);

    void deleteAccount(String email);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();
}
