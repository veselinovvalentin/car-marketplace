package main.web;

import main.model.ListingStatus;
import main.model.User;
import main.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ModelAndView getProfilePage(Principal principal) {
        if (principal == null) {
            return new ModelAndView("redirect:/login");
        }

        String username = principal.getName();
        User user = userService.getByUsername(username);

        int totalListings = user.getListings().size();

        long activeListings = user.getListings().stream()
                .filter(listing -> listing.getStatus() == ListingStatus.APPROVED)
                .count();

        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("listings", user.getListings());
        modelAndView.addObject("totalListings", totalListings);
        modelAndView.addObject("activeListings", activeListings);

        return modelAndView;
    }
}
