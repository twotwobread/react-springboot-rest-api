package org.prgrms.be.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.prgrms.be.app.controller.dto.ProductDto;
import org.prgrms.be.app.domain.product.CategoryType;
import org.prgrms.be.app.domain.product.Product;
import org.prgrms.be.app.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Slf4j
@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/admin/product/new/{brandId}")
    public String createFormProduct(Model model, @PathVariable UUID brandId) {
        model.addAttribute("brandId", brandId);
        return "product/new_product";
    }

    @PostMapping("/admin/product/new")
    public String createProduct(ProductDto request) {
        CategoryType categoryType = CategoryType.of(request.category());
        productService.createProduct(request.name(), UUID.fromString(request.brandId()), request.price(), categoryType, request.stockQuantity());
        return "redirect:/admin/brands/" + request.brandId();
    }

    @PostMapping("/admin/products")
    public String deleteProduct(@RequestParam("productId") UUID productId, String brandId) {
        productService.deleteProduct(productId);
        return "redirect:/admin/brands/" + brandId;
    }


    @GetMapping("/main/product/{productId}")
    public String detailProduct(Model model, @PathVariable UUID productId) {
        Product product = productService.findById(productId);
        model.addAttribute("product", product);
        return "product/product_detail";
    }
}
