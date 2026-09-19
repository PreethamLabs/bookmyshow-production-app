package com.example.service.impl;

import com.example.dto.request.UserCreateRequest;
import com.example.dto.request.response.UserResponse;
import com.example.entity.User;
import com.example.enums.AccountStatus;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.example.service.NotificationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final NotificationClient notificationClient;

    @Override
    public UserResponse createUser(UserCreateRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT,
                    "An account with this email already exists. Please sign in."
            );
        }

        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT,
                    "An account with this phone number already exists. Please use a different phone number or sign in."
            );
        }

        User user = userMapper.toEntity(request);

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setAccountStatus(AccountStatus.NEW);

        try {
            User savedUser = userRepository.save(user);
            return userMapper.toResponse(savedUser);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT,
                    "An account with this email or phone number already exists. Please sign in."
            );
        }
    }

    @Override
    public void sendVerificationCode(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Account not found"));
        notificationClient.sendOtp(email);
    }

    @Override
    public void verifyEmail(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Account not found"));

        if (!notificationClient.verifyOtp(email, otp)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid or expired OTP");
        }

        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
    }

    @Override
    public void deleteAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Account not found"));

        String accountId = UUID.randomUUID().toString();
        user.setName("Deleted User");
        user.setEmail("deleted-" + accountId + "@deleted.marquee.invalid");
        user.setPhoneNumber("+999" + accountId.replace("-", "").substring(0, 12));
        user.setPassword(passwordEncoder.encode(accountId));
        user.setAccountStatus(AccountStatus.BLOCKED);
        userRepository.save(user);
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

}
