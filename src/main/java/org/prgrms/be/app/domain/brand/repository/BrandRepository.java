package org.prgrms.be.app.domain.brand.repository;

import org.prgrms.be.app.domain.brand.Brand;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BrandRepository {
    Brand insert(Brand brand);

    UUID delete(UUID brandId);

    Brand update(Brand brand);

    Optional<Brand> findById(UUID brandId);

    List<Brand> getAllBrand();

    List<Brand> findBrandsByName(String name);

    void clear();
}
