package main.web.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import main.model.CarBodyType;
import main.model.FuelType;
import main.model.TransmissionType;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CreateListingRequest {

    @NotBlank
    private String title;

    @NotNull
    private UUID brandId;

    @NotNull
    private UUID modelId;

    @NotNull
    @Min(1900)
    private Integer year;

    @NotNull
    @Positive
    private Integer mileageKm;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotNull
    private FuelType fuelType;

    @NotNull
    private TransmissionType transmission;

    private CarBodyType bodyType;

    private String engine;

    @NotBlank
    private String location;

    @NotBlank
    private String description;

    private String mainImageUrl;
}
