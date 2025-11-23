package main.service;

import lombok.extern.slf4j.Slf4j;
import main.exception.DomainException;
import main.model.User;
import main.model.UserRole;
import main.repository.UserRepository;
import main.web.dto.LoginRequest;
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

    public User getById(UUID id) {

        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with [%s] id does not exist.".formatted(id)));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User with [%s] does not exist.".formatted(username)));
    }


}
