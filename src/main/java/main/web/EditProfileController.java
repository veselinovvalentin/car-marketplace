package main.web;


import main.model.User;
import main.security.CustomUserDetails;
import main.service.UserService;
import main.web.dto.ChangePasswordDto;
import main.web.dto.EditProfileDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
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


    @GetMapping("/changepassword")
    public String showChangePasswordPage(@AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        User user = currentUser.getUser();
        model.addAttribute("changePassword", new ChangePasswordDto());
        model.addAttribute("user", user);
        return "changepassword";
    }

    @PostMapping("/changepassword")
    public String changePassword(@ModelAttribute("changePassword") ChangePasswordDto dto,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();

        try {
            userService.changePassword(username, dto.getCurrentPassword(), dto.getNewPassword());
            redirectAttributes.addFlashAttribute("successMessage", "Your password has been updated successfully.");
            return "redirect:/profile";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/changepassword";
        }
    }


}
