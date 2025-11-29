package main.web;

import main.model.Listing;
import main.model.User;
import main.service.ListingService;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class CarsController {

    private final UserService userService;
    private final ListingService listingService;

    @Autowired
    public CarsController(UserService userService, ListingService listingService) {
        this.userService = userService;
        this.listingService = listingService;
    }

    @GetMapping("/cars")
    public ModelAndView getCarsPage(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("cars");

        if (principal != null) {
            String username = principal.getName();
            User user = userService.getByUsername(username);
            modelAndView.addObject("user", user);
        }

        modelAndView.addObject("listings", listingService.getFeaturedListings());

        return modelAndView;
    }

    @GetMapping("/cars/{id}")
    public ModelAndView getCarDetails(@PathVariable UUID id, Principal principal) {
        Listing listing = listingService.getById(id);

        ModelAndView modelAndView = new ModelAndView("cardetails");
        modelAndView.addObject("listing", listing);

        if (principal != null) {
            String username = principal.getName();
            User user = userService.getByUsername(username);
            modelAndView.addObject("user", user);
        }

        List<Listing> others = listingService.getFeaturedListings()
                .stream()
                .filter(l -> !l.getId().equals(id))
                .toList();
        modelAndView.addObject("otherListings", others);

        return modelAndView;
    }
}

