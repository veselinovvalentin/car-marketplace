package main.web.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Full name is required.")
    @Size(min = 2, max = 80, message = "Full name must be between 2 and 80 characters.")
    private String fullName;

    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]*$",
            message = "Username must start with a letter and contain only letters, numbers or underscore."
    )
    private String username;

    @NotBlank(message = "Email is required.")
    @Size(max = 120, message = "Email must be at most 120 characters.")
    private String email;

    @Size(max = 20, message = "Phone number must be at most 20 characters.")
    private String phoneNumber;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 32, message = "Password must be between 6 and 32 characters.")
    private String password;


    @NotBlank(message = "Please confirm your password.")
    private String confirmPassword;

    @AssertTrue(message = "You must accept the Terms of Use.")
    private boolean termsAccepted;

}
