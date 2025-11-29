package main.web;

import main.model.Listing;
import main.model.User;
import main.service.BrandService;
import main.service.CarModelService;
import main.service.ListingService;
import main.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ListingService listingService;
    private final BrandService brandService;
    private final CarModelService carModelService;
    private final UserService userService;

    public AdminController(ListingService listingService,
                           BrandService brandService,
                           CarModelService carModelService,
                           UserService userService) {
        this.listingService = listingService;
        this.brandService = brandService;
        this.carModelService = carModelService;
        this.userService = userService;
    }

    @GetMapping
    public String showDashboard(Model model,
                                Principal principal,
                                @ModelAttribute("brandError") String brandError,
                                @ModelAttribute("brandSuccess") String brandSuccess,
                                @ModelAttribute("modelError") String modelError,
                                @ModelAttribute("modelSuccess") String modelSuccess) {

        if (principal != null) {
            String username = principal.getName();
            User user = userService.getByUsername(username);
            model.addAttribute("user", user);
        }

        List<Listing> pendingListings = listingService.getPendingListings();
        model.addAttribute("pendingListings", pendingListings);
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("models", carModelService.findAll());

        if (brandError != null && !brandError.isEmpty()) {
            model.addAttribute("brandError", brandError);
        }
        if (brandSuccess != null && !brandSuccess.isEmpty()) {
            model.addAttribute("brandSuccess", brandSuccess);
        }
        if (modelError != null && !modelError.isEmpty()) {
            model.addAttribute("modelError", modelError);
        }
        if (modelSuccess != null && !modelSuccess.isEmpty()) {
            model.addAttribute("modelSuccess", modelSuccess);
        }

        return "admin";
    }

    @PostMapping("/brands")
    public String addBrand(@RequestParam("name") String name,
                           RedirectAttributes redirectAttributes) {
        try {
            brandService.createBrand(name);
            redirectAttributes.addFlashAttribute("brandSuccess", "Brand added successfully");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("brandError", ex.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/brands/{id}/delete")
    public String deleteBrand(@PathVariable("id") UUID id,
                              RedirectAttributes redirectAttributes) {
        try {
            brandService.deleteBrandById(id);
            redirectAttributes.addFlashAttribute("brandSuccess", "Brand deleted");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("brandError", "Cannot delete brand: " + ex.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/models")
    public String addModel(@RequestParam("brandId") UUID brandId,
                           @RequestParam("name") String name,
                           RedirectAttributes redirectAttributes) {
        try {
            carModelService.createModel(brandId, name);
            redirectAttributes.addFlashAttribute("modelSuccess", "Model added successfully");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("modelError", ex.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/models/{id}/delete")
    public String deleteModel(@PathVariable("id") UUID id,
                              RedirectAttributes redirectAttributes) {
        try {
            carModelService.deleteModelById(id);
            redirectAttributes.addFlashAttribute("modelSuccess", "Model deleted");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("modelError", "Cannot delete model: " + ex.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/listings/{id}/approve")
    public String approveListing(@PathVariable("id") UUID id,
                                 RedirectAttributes redirectAttributes) {
        listingService.approveListing(id);
        redirectAttributes.addFlashAttribute("listingSuccess", "Listing approved");
        return "redirect:/admin";
    }

    @PostMapping("/listings/{id}/reject")
    public String rejectListing(@PathVariable("id") UUID id,
                                RedirectAttributes redirectAttributes) {
        listingService.rejectListing(id);
        redirectAttributes.addFlashAttribute("listingSuccess", "Listing rejected");
        return "redirect:/admin";
    }
}
