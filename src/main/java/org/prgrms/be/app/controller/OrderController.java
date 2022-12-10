package org.prgrms.be.app.controller;

import org.prgrms.be.app.controller.dto.OrderDto;
import org.prgrms.be.app.controller.dto.OrderProductDto;
import org.prgrms.be.app.domain.member.Member;
import org.prgrms.be.app.domain.order.Order;
import org.prgrms.be.app.domain.order.dto.OrderServiceDto;
import org.prgrms.be.app.domain.order_product.OrderProduct;
import org.prgrms.be.app.service.MemberService;
import org.prgrms.be.app.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;

    public OrderController(OrderService orderService, MemberService memberService) {
        this.orderService = orderService;
        this.memberService = memberService;
    }

    @PostMapping("/main/orders")
    public String getOrdersByMemberId(Model model, @RequestParam("memberId") UUID memberId) {
        List<Order> orders = orderService.getOrderByMemberId(memberId);
        model.addAttribute("orders", orders);
        return "order/orders";
    }

    @GetMapping("/main/orders/{orderId}")
    public String getOrderProducts(Model model, @PathVariable UUID orderId) {
        List<OrderProduct> orderProducts = orderService.getOrderProductByOrderId(orderId);
        model.addAttribute("orderProducts", orderProducts);
        return "order/order_products";
    }

    @PostMapping("/main/orders/{orderId}")
    public String deleteOrder(@RequestParam("orderId") UUID orderId) {
        orderService.deleteOrder(orderId);
        return "redirect:/main";
    }

    @PostMapping("/main/order/order_products/{productId}")
    public String deleteOrderProduct(OrderProductDto orderProductDto) {
        orderService.deleteOrderProduct(orderProductDto.orderProductId(),
                new OrderServiceDto(orderProductDto.productId(), orderProductDto.count()));
        return "redirect:/main";
    }

    @GetMapping("/main/order/products/{productId}")
    public String createFormOrder(Model model, @PathVariable UUID productId) {
        List<Member> allMember = memberService.getAllMember();
        model.addAllAttributes(Map.of("productId", productId,
                "members", allMember));
        return "order/new_order";
    }

    @PostMapping("/main/order/new")
    public String createOrder(OrderDto request) {
        List<OrderServiceDto> orderServiceDtos = Arrays.asList(new OrderServiceDto(request.productId(), request.count()));
        orderService.createOrderByProduct(request.memberId(), orderServiceDtos);
        return "redirect:/main";
    }
}
