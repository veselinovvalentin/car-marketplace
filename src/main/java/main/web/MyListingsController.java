package main.web;

import jakarta.validation.Valid;
import main.model.Listing;
import main.model.User;
import main.service.BrandService;
import main.service.CarModelService;
import main.service.ListingService;
import main.service.UserService;
import main.web.dto.EditListingDto;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.UUID;

@Controller
public class MyListingsController {

    private final UserService userService;
    private final ListingService listingService;
    private final BrandService brandService;
    private final CarModelService carModelService;

    public MyListingsController(UserService userService,
                                ListingService listingService,
                                BrandService brandService,
                                CarModelService carModelService) {
        this.userService = userService;
        this.listingService = listingService;
        this.brandService = brandService;
        this.carModelService = carModelService;
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

    @GetMapping("/my-listings/{id}/edit")
    public ModelAndView editListingForm(@PathVariable("id") UUID id, Principal principal) {
        if (principal == null) {
            return new ModelAndView("redirect:/login");
        }

        String username = principal.getName();
        User user = userService.getByUsername(username);

        Listing listing = listingService.getById(id);

        if (!listing.getOwner().getId().equals(user.getId())) {
            return new ModelAndView("redirect:/my-listings");
        }

        EditListingDto dto = new EditListingDto();
        dto.setTitle(listing.getTitle());
        dto.setLocation(listing.getLocation());
        dto.setBrandId(listing.getBrand().getId());
        dto.setModelId(listing.getModel().getId());
        dto.setYear(listing.getYear());
        dto.setMileageKm(listing.getMileageKm());
        dto.setFuelType(listing.getFuelType());
        dto.setTransmission(listing.getTransmission());
        dto.setBodyType(listing.getBodyType());
        dto.setEngine(listing.getEngine());
        dto.setPrice(listing.getPrice());
        dto.setDescription(listing.getDescription());
        dto.setMainImageUrl(listing.getMainImageUrl());

        ModelAndView mav = new ModelAndView("edit-listing");
        mav.addObject("user", user);
        mav.addObject("listing", listing);
        mav.addObject("editListingRequest", dto);
        mav.addObject("brands", brandService.findAll());
        mav.addObject("models", carModelService.findAll());

        return mav;
    }

    @PostMapping("/my-listings/{id}/edit")
    public ModelAndView editListingSubmit(@PathVariable("id") UUID id,
                                          @Valid @ModelAttribute("editListingRequest") EditListingDto editListingRequest,
                                          BindingResult bindingResult,
                                          Principal principal) {
        if (principal == null) {
            return new ModelAndView("redirect:/login");
        }

        String username = principal.getName();
        User user = userService.getByUsername(username);

        if (bindingResult.hasErrors()) {
            Listing listing = listingService.getById(id);

            ModelAndView mav = new ModelAndView("edit-listing");
            mav.addObject("user", user);
            mav.addObject("listing", listing);
            mav.addObject("brands", brandService.findAll());
            mav.addObject("models", carModelService.findAll());
            return mav;
        }

        listingService.updateListing(user, id, editListingRequest);

        return new ModelAndView("redirect:/my-listings");
    }
}
