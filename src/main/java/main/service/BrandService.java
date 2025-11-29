package main.service;

import main.model.Brand;
import main.repository.BrandRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> findAll() {
        return brandRepository.findAll(Sort.by("name").ascending());
    }

    public Brand createBrand(String name) {
        String trimmed = name == null ? null : name.trim();
        if (trimmed == null || trimmed.isEmpty()) {
            throw new IllegalArgumentException("Brand name cannot be empty");
        }

        if (brandRepository.existsByNameIgnoreCase(trimmed)) {
            throw new IllegalArgumentException("Brand already exists: " + trimmed);
        }

        Brand brand = Brand.builder()
                .name(trimmed)
                .build();

        return brandRepository.save(brand);
    }

    public void deleteBrandById(java.util.UUID id) {
        brandRepository.deleteById(id);
    }
}
