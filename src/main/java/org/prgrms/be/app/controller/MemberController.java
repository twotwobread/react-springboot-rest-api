package org.prgrms.be.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.prgrms.be.app.controller.dto.MemberDto;
import org.prgrms.be.app.domain.Address;
import org.prgrms.be.app.domain.member.Member;
import org.prgrms.be.app.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/admin/members")
    public String getAllMember(Model model) {
        List<Member> allMember = memberService.getAllMember();
        model.addAttribute("members", allMember);
        return "member/members";
    }

    @PostMapping("/admin/members")
    public String deleteMember(@RequestParam("memberId") UUID memberId) {
        memberService.deleteMember(memberId);
        return "redirect:/admin/members";
    }


    @GetMapping("/main/member/new")
    public String createFormMember() {
        return "member/new_member";
    }

    @PostMapping("/main/member/new")
    public String createMember(MemberDto request) {
        memberService.createMember(request.name(), new Address(request.address(), request.details(), request.zipcode()));
        return "redirect:/main";
    }
}