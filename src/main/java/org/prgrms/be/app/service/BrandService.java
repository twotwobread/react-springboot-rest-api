package org.prgrms.be.app.service;

import org.prgrms.be.app.domain.Address;
import org.prgrms.be.app.domain.brand.Brand;
import org.prgrms.be.app.domain.brand.repository.BrandRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Brand createBrand(String name, Address address) {
        return brandRepository.insert(Brand.create(name, address));
    }

    public List<Brand> getAllBrand() {
        return brandRepository.getAllBrand();
    }

    public Brand findById(UUID brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 브랜드입니다."));
    }

    public UUID deleteBrand(UUID brandId) {
        return brandRepository.delete(brandId);
    }

}
