package org.prgrms.be.app.service;

import org.prgrms.be.app.domain.Address;
import org.prgrms.be.app.domain.member.Member;
import org.prgrms.be.app.domain.member.repository.MemberRepository;
import org.prgrms.be.app.domain.order.Order;
import org.prgrms.be.app.domain.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    public MemberService(MemberRepository memberRepository, OrderRepository orderRepository) {
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
    }

    public Member createMember(String name, Address address) {
        return memberRepository.insert(Member.create(name, address));
    }

    public List<Member> getAllMember() {
        return memberRepository.getAllMember();
    }

    public List<Member> findMembersByName(String name) {
        return memberRepository.findMembersByName(name);
    }

    public Member findMemberById(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("요청한 Id의 member가 존재하지 않습니다."));
    }

    public UUID deleteMember(UUID memberId) throws NoSuchElementException {
        memberRepository.findById(memberId).orElseThrow(() -> new NoSuchElementException("요청한 Id의 member가 존재하지 않습니다."));
        return memberRepository.delete(memberId);
    }

    public Member updateAddress(UUID memberId, Address address) {
        Member memberById = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("요청한 Id의 member가 존재하지 않습니다."));
        memberById.setAddress(address);
        return memberRepository.update(memberById);
    }

    public List<Order> orderHistoryByMemberId(UUID memberId) {
        return orderRepository.findByMemberId(memberId);
    }
}
