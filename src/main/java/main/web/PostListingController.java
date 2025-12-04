package main.web;

import jakarta.validation.Valid;
import main.model.User;
import main.repository.BrandRepository;
import main.repository.CarModelRepository;
import main.service.ListingService;
import main.service.UserService;
import main.web.dto.CreateListingRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class PostListingController {

    private final UserService userService;
    private final ListingService listingService;
    private final BrandRepository brandRepository;
    private final CarModelRepository carModelRepository;

    public PostListingController(UserService userService,
                                 ListingService listingService,
                                 BrandRepository brandRepository,
                                 CarModelRepository carModelRepository) {
        this.userService = userService;
        this.listingService = listingService;
        this.brandRepository = brandRepository;
        this.carModelRepository = carModelRepository;
    }

    @GetMapping("/postlisting")
    public ModelAndView getListingPage(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("postlisting");
        modelAndView.addObject("createListingRequest", new CreateListingRequest());
        modelAndView.addObject("brands", brandRepository.findAll());
        modelAndView.addObject("models", carModelRepository.findAll());

        if (principal != null) {
            String username = principal.getName();
            User user = userService.getByUsername(username);
            modelAndView.addObject("user", user);
        }

        return modelAndView;
    }

    @PostMapping("/postlisting")
    public String handleCreateListing(
            @Valid @ModelAttribute("createListingRequest") CreateListingRequest request,
            BindingResult bindingResult,
            Principal principal,
            Model model) {

        if (principal == null) {
            return "redirect:/login";
        }


        if (bindingResult.hasErrors()) {
            model.addAttribute("brands", brandRepository.findAll());
            model.addAttribute("models", carModelRepository.findAll());
            return "postlisting";
        }

        String username = principal.getName();
        User user = userService.getByUsername(username);

        try {
            listingService.createListing(user, request);
        } catch (RuntimeException ex) {
            bindingResult.reject("listingError", ex.getMessage());

            model.addAttribute("brands", brandRepository.findAll());
            model.addAttribute("models", carModelRepository.findAll());
            return "postlisting";
        }

        return "redirect:/cars";
    }
}
