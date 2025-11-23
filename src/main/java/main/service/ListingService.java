package main.service;

import lombok.extern.slf4j.Slf4j;
import main.model.Listing;
import main.model.ListingStatus;
import main.model.User;
import main.repository.ListingRepository;
import main.web.dto.CreateListingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ListingService {
    private final ListingRepository repository;

    @Autowired
    public ListingService(ListingRepository repository) {
        this.repository = repository;
    }


    public Listing createListing(User user,CreateListingRequest listingRequest) {

        Listing listing = Listing.builder()
                .owner(user)
                .title(listingRequest.getTitle())
                .brand(listingRequest.getBrand())
                .model(listingRequest.getModel())
                .year(listingRequest.getYear())
                .price(listingRequest.getPrice())
                .mileageKm(listingRequest.getMileageKm())
                .fuelType(listingRequest.getFuelType())
                .transmission(listingRequest.getTransmission())
                .bodyType(listingRequest.getBodyType())
                .location(listingRequest.getLocation())
                .description(listingRequest.getDescription())
                .mainImageUrl(listingRequest.getMainImageUrl())
                .status(ListingStatus.DRAFT)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        log.info("Listing [{}] created for user [{}]", listing.getId(), user.getUsername());

        return repository.save(listing);
    }


    public List<Listing> getFeaturedListings() {
        return repository.findByStatusAndActiveTrue(ListingStatus.APPROVED);
    }
}
