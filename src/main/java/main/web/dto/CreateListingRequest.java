package main.web.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.List;

import main.model.CarBodyType;
import main.model.FuelType;
import main.model.TransmissionType;
import main.model.ListingStatus;

@Data
public class CreateListingRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String location;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotNull
    @Min(1900)
    @Max(2100)
    private Integer year;

    @NotNull
    @Min(0)
    private Integer mileageKm ;

    @NotNull
    private FuelType fuelType;

    @NotNull
    private TransmissionType transmission;

    @NotNull
    private CarBodyType bodyType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @NotBlank
    private String description;

    private String mainImageUrl;


}
