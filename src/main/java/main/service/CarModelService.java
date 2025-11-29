package main.service;

import main.model.Brand;
import main.model.CarModel;
import main.repository.BrandRepository;
import main.repository.CarModelRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CarModelService {

    private final CarModelRepository carModelRepository;
    private final BrandRepository brandRepository;

    public CarModelService(CarModelRepository carModelRepository,
                           BrandRepository brandRepository) {
        this.carModelRepository = carModelRepository;
        this.brandRepository = brandRepository;
    }

    public List<CarModel> findAll() {
        return carModelRepository.findAll(Sort.by("brand.name").ascending().and(Sort.by("name")));
    }

    public CarModel createModel(UUID brandId, String name) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new IllegalArgumentException("Brand not found: " + brandId));

        String trimmed = name == null ? null : name.trim();
        if (trimmed == null || trimmed.isEmpty()) {
            throw new IllegalArgumentException("Model name cannot be empty");
        }

        CarModel model = CarModel.builder()
                .brand(brand)
                .name(trimmed)
                .build();

        return carModelRepository.save(model);
    }

    public void deleteModelById(UUID id) {
        carModelRepository.deleteById(id);
    }
}
