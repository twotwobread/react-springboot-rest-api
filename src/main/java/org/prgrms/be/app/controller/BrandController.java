package org.prgrms.be.app.controller;

import org.prgrms.be.app.controller.dto.MemberDto;
import org.prgrms.be.app.domain.Address;
import org.prgrms.be.app.domain.brand.Brand;
import org.prgrms.be.app.domain.product.Product;
import org.prgrms.be.app.service.BrandService;
import org.prgrms.be.app.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class BrandController {
    private final BrandService brandService;
    private final ProductService productService;

    public BrandController(BrandService brandService, ProductService productService) {
        this.brandService = brandService;
        this.productService = productService;
    }

    @GetMapping("/admin/brands/{brandId}")
    public String getBrandProductsManagement(Model model, @PathVariable UUID brandId) {
        List<Product> byBrandId = productService.findByBrandId(brandId);
        Brand byId = brandService.findById(brandId);
        model.addAllAttributes(Map.of(
                "products", byBrandId,
                "brand", byId));
        return "brand/brand_management";
    }

    @PostMapping("/admin/brands")
    public String deleteBrand(String brandId) {
        brandService.deleteBrand(UUID.fromString(brandId));
        return "redirect:/admin/brands";
    }

    @GetMapping("/admin/brands")
    public String getAllBrands(Model model) {
        List<Brand> allBrand = brandService.getAllBrand();
        model.addAttribute("brands", allBrand);
        return "brand/brands";
    }

    @GetMapping("/admin/brands/new")
    public String createFormBrand() {
        return "brand/new_brand";
    }

    @PostMapping("/admin/brands/new")
    public String createBrand(MemberDto request) {
        brandService.createBrand(request.name(), new Address(request.address(), request.details(), request.zipcode()));
        return "redirect:/admin/brands";
    }
}
