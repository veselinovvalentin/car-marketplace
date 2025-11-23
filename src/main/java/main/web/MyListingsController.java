package main.web;

import main.model.User;
import main.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class MyListingsController {

    private final UserService userService;

    public MyListingsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my-listings")
    public ModelAndView myListings(Principal principal) {
        if (principal == null) {
            return new ModelAndView("redirect:/login");
        }

        String username = principal.getName();
        User user = userService.getByUsername(username);

        ModelAndView mav = new ModelAndView("my-listings");
        mav.addObject("user", user);
        mav.addObject("listings", user.getListings());
        return mav;
    }
}
