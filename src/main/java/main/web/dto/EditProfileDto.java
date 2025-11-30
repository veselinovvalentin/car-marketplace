package main.web.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditProfileDto {
    @NotBlank
    private String fullName;
    @NotBlank
    private String username;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String location;
}
