package org.prgrms.be.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.prgrms.be.app.domain.brand.Brand;
import org.prgrms.be.app.domain.member.Member;
import org.prgrms.be.app.domain.product.CategoryType;
import org.prgrms.be.app.domain.product.Product;
import org.prgrms.be.app.service.BrandService;
import org.prgrms.be.app.service.MemberService;
import org.prgrms.be.app.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Controller
public class MainPageController {

    private final ProductService productService;
    private final MemberService memberService;
    private final BrandService brandService;

    public MainPageController(ProductService productService, MemberService memberService, BrandService brandService) {
        this.productService = productService;
        this.memberService = memberService;
        this.brandService = brandService;
    }

    @GetMapping("/main")
    public String mainPage(Model model) {
        List<Product> products = productService.getAllProduct();
        List<Member> members = memberService.getAllMember();
        List<Brand> brands = brandService.getAllBrand();
        model.addAllAttributes(Map.of(
                "products", products,
                "members", members,
                "brands", brands
        ));
        return "main_page";
    }

    @PostMapping("/main")
    public String productsByBrandIdAndCategory(Model model,
                                               @RequestParam(required = false, value = "brandId") String brandId,
                                               @RequestParam(required = false, value = "category") String category
    ) {
        log.warn("{} {}", brandId, category);
        CategoryType categoryType = null;
        UUID brandUuid = null;
        if (!Objects.equals(brandId, "")) {
            brandUuid = UUID.fromString(brandId);
        }
        if (!Objects.equals(category, "")) {
            categoryType = CategoryType.of(category);
        }

        List<Product> products = productService.findByBrandIdAndCategory(brandUuid, categoryType);
        List<Member> members = memberService.getAllMember();
        List<Brand> brands = brandService.getAllBrand();
        model.addAllAttributes(Map.of(
                "products", products,
                "members", members,
                "brands", brands
        ));
        return "main_page";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin_page";
    }
}
