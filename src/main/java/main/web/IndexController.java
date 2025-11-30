package main.web;

import jakarta.validation.Valid;
import main.model.Listing;
import main.model.User;
import main.service.ListingService;
import main.service.UserService;
import main.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class IndexController {

    private final UserService userService;
    private final ListingService listingService;

    @Autowired
    public IndexController(UserService userService, ListingService listingService) {
        this.userService = userService;
        this.listingService = listingService;
    }

    @GetMapping("/")
    public ModelAndView getHomePage(Principal principal) {
        ModelAndView mav = new ModelAndView("index");

        if (principal != null) {
            String username = principal.getName();
            User user = userService.getByUsername(username);
            mav.addObject("user", user);
        }

        List<Listing> featured = listingService.getFeaturedListings();
        mav.addObject("featuredListings", featured);

        return mav;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid RegisterRequest registerRequest,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }

        userService.register(registerRequest);
        redirectAttributes.addFlashAttribute("successfulRegistration", "You have registered successfully");

        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        return new ModelAndView("login");
    }


    @GetMapping("/cardetails")
    public ModelAndView getCarDetailsPage() {
        return new ModelAndView("cardetails");
    }
}
