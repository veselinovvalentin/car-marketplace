package main.service;

import lombok.extern.slf4j.Slf4j;
import main.model.*;
import main.repository.BrandRepository;
import main.repository.CarModelRepository;
import main.repository.ListingRepository;
import main.web.dto.CreateListingRequest;
import main.web.dto.EditListingDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ListingService {

    private final ListingRepository listingRepository;
    private final BrandRepository brandRepository;
    private final CarModelRepository carModelRepository;

    public ListingService(ListingRepository listingRepository,
                          BrandRepository brandRepository,
                          CarModelRepository carModelRepository) {
        this.listingRepository = listingRepository;
        this.brandRepository = brandRepository;
        this.carModelRepository = carModelRepository;
    }

    public Listing createListing(User user, CreateListingRequest listingRequest) {

        Brand brand = brandRepository.findById(listingRequest.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found: " + listingRequest.getBrandId()));

        CarModel model = carModelRepository.findById(listingRequest.getModelId())
                .orElseThrow(() -> new RuntimeException("Model not found: " + listingRequest.getModelId()));

        if (!model.getBrand().getId().equals(brand.getId())) {
            throw new RuntimeException("Model does not belong to selected brand.");
        }

        Listing listing = Listing.builder()
                .owner(user)
                .title(listingRequest.getTitle())
                .brand(brand)
                .model(model)
                .year(listingRequest.getYear())
                .price(listingRequest.getPrice())
                .mileageKm(listingRequest.getMileageKm())
                .fuelType(listingRequest.getFuelType())
                .transmission(listingRequest.getTransmission())
                .bodyType(listingRequest.getBodyType())
                .engine(listingRequest.getEngine())
                .location(listingRequest.getLocation())
                .description(listingRequest.getDescription())
                .mainImageUrl(listingRequest.getMainImageUrl())
                .status(ListingStatus.PENDING)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        listing = listingRepository.save(listing);

        log.info("Listing [{}] created for user [{}]", listing.getId(), user.getUsername());

        return listing;
    }


    public Listing updateListing(User user, UUID id, EditListingDto dto) {

        Listing listing = getById(id);

        if (!listing.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to edit this listing.");
        }

        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found: " + dto.getBrandId()));

        CarModel model = carModelRepository.findById(dto.getModelId())
                .orElseThrow(() -> new RuntimeException("Model not found: " + dto.getModelId()));

        if (!model.getBrand().getId().equals(brand.getId())) {
            throw new RuntimeException("Model does not belong to selected brand.");
        }

        listing.setTitle(dto.getTitle());
        listing.setBrand(brand);
        listing.setModel(model);
        listing.setYear(dto.getYear());
        listing.setMileageKm(dto.getMileageKm());
        listing.setPrice(dto.getPrice());
        listing.setFuelType(dto.getFuelType());
        listing.setTransmission(dto.getTransmission());
        listing.setBodyType(dto.getBodyType());
        listing.setEngine(dto.getEngine());
        listing.setLocation(dto.getLocation());
        listing.setDescription(dto.getDescription());
        listing.setMainImageUrl(dto.getMainImageUrl());
        listing.setUpdatedAt(LocalDateTime.now());

        listing = listingRepository.save(listing);

        log.info("Listing [{}] updated by user [{}]", listing.getId(), user.getUsername());

        return listing;
    }

    public List<Listing> getFeaturedListings() {
        return listingRepository.findByStatusAndActiveTrue(ListingStatus.APPROVED);
    }

    public Listing getById(UUID id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found: " + id));
    }

    public List<Listing> getPendingListings() {
        return listingRepository.findByStatusAndActiveTrue(ListingStatus.PENDING);
    }


    public void approveListing(UUID id) {
        Listing listing = getById(id);
        listing.setStatus(ListingStatus.APPROVED);
        listing.setUpdatedAt(java.time.LocalDateTime.now());
        listingRepository.save(listing);
    }

    public void rejectListing(UUID id) {
        Listing listing = getById(id);
        listing.setStatus(ListingStatus.REJECTED);
        listing.setUpdatedAt(java.time.LocalDateTime.now());
        listingRepository.save(listing);
    }
}

