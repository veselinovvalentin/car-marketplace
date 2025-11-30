package main.web;


import main.model.User;
import main.security.CustomUserDetails;
import main.service.UserService;
import main.web.dto.EditProfileDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class EditProfileController {

    private final UserService userService;


    public EditProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/editprofile")
    public String showEditProfile(@AuthenticationPrincipal CustomUserDetails currentUser,
                                  Model model) {

        User user = currentUser.getUser();

        EditProfileDto dto = new EditProfileDto();
        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setLocation(user.getLocation());

        model.addAttribute("editProfile", dto);
        model.addAttribute("user", user);

        return "editprofile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@AuthenticationPrincipal CustomUserDetails currentUser,
                                @ModelAttribute("editProfile") EditProfileDto dto) {

        UUID userId = currentUser.getUser().getId();
        userService.updateProfile(userId, dto);

        return "redirect:/profile";
    }
}
