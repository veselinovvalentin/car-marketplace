package main.repository;

import main.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarModelRepository extends JpaRepository<CarModel, UUID> {
}
