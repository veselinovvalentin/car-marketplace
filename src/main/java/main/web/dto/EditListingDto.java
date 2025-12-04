package main.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import main.model.CarBodyType;
import main.model.FuelType;
import main.model.TransmissionType;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class EditListingDto {

    @NotBlank
    private String title;

    @NotBlank
    private String location;

    @NotNull
    private UUID brandId;

    @NotNull
    private UUID modelId;

    @NotNull
    @Min(1900)
    private Integer year;

    @NotNull
    @Min(0)
    private Integer mileageKm;

    @NotNull
    private FuelType fuelType;

    @NotNull
    private TransmissionType transmission;

    private CarBodyType bodyType;

    private String engine;

    @NotNull
    private BigDecimal price;

    @NotBlank
    private String description;

    private String mainImageUrl;
}
