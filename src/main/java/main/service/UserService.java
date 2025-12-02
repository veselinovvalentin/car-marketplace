package main.service;

import lombok.extern.slf4j.Slf4j;
import main.model.User;
import main.model.UserRole;
import main.repository.UserRepository;
import main.web.dto.EditProfileDto;
import main.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User register(RegisterRequest registerRequest) {


        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match.");
        }

        Optional<User> optionalUser = userRepository.findByUsername(registerRequest.getUsername());
        if (optionalUser.isPresent()) {
            throw new RuntimeException("User with [%s] username already exist.".formatted(registerRequest.getUsername()));

        }

        User user = User.builder()
                .fullName(registerRequest.getFullName())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .phoneNumber(registerRequest.getPhoneNumber())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        user = userRepository.save(user);

        log.info("New user profile was registered in the system for user [%s].".formatted(registerRequest.getUsername()));

        return user;
    }

    public void updateProfile(UUID userId, EditProfileDto dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setLocation(dto.getLocation());

        userRepository.save(user);
    }

    public void changePassword(String username, String oldPassword, String newPassword) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));


        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Your current password is incorrect.");
        }


        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the old one.");
        }


        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters long.");
        }


        String encoded = passwordEncoder.encode(newPassword);
        user.setPassword(encoded);
        userRepository.save(user);
    }


    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User with [%s] does not exist.".formatted(username)));
    }


}
