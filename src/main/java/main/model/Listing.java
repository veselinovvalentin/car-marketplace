package main.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


    @Entity
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
public class Listing {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        private User owner;

        @Column(nullable = false)
        private String title;

        @Column(nullable = false)
        private String brand;

        @Column(nullable = false)
        private String model;

        @Column(nullable = false)
        private Integer year;

        @Column(nullable = false)
        private Integer mileageKm;


        @Column(nullable = false,scale = 2)
        private BigDecimal price;


        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 20)
        private FuelType fuelType;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 20)
        private TransmissionType transmission;

        @Enumerated(EnumType.STRING)
        @Column(length = 40)
        private CarBodyType bodyType;

        @Column(length = 40)
        private String engine;

        @Column(nullable = false, length = 100)
        private String location;


        @Column(nullable = false, length = 4000)
        private String description;


        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 20)
        private ListingStatus status = ListingStatus.DRAFT;

        @Column(nullable = false)
        private boolean active = true;


        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt = LocalDateTime.now();

        @Column(nullable = false)
        private LocalDateTime updatedAt = LocalDateTime.now();


        @Column(length = 255)
        private String mainImageUrl;
}
