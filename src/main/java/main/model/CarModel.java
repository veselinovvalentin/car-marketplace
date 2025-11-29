package main.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 80)
    private String name;

    @ManyToOne(optional = false)
    private Brand brand;

    @OneToMany(mappedBy = "model")
    @Builder.Default
    private List<Listing> listings = new ArrayList<>();
}
