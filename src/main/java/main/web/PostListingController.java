package main.web;

import jakarta.validation.Valid;
import main.model.User;
import main.service.ListingService;
import main.service.UserService;
import main.web.dto.CreateListingRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class PostListingController {

    private final UserService userService;
    private final ListingService listingService;

    public PostListingController(UserService userService, ListingService listingService) {
        this.userService = userService;
        this.listingService = listingService;
    }

    @GetMapping("/postlisting")
    public ModelAndView getListingPage(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("postlisting");
        modelAndView.addObject("createListingRequest", new CreateListingRequest());


        if (principal != null) {
            String username = principal.getName();
            User user = userService.getByUsername(username);
            modelAndView.addObject("user", user);
        }

        return modelAndView;
    }

    @PostMapping("/postlisting")
    public String handleCreateListing(
            @Valid CreateListingRequest request,
            BindingResult bindingResult,
            Principal principal) {

        if (bindingResult.hasErrors()) {
            return "postlisting";
        }

        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        User user = userService.getByUsername(username);

        listingService.createListing(user, request);

        return "redirect:/cars";
    }
}
