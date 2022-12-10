package org.prgrms.be.app.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.be.app.domain.Address;
import org.prgrms.be.app.domain.member.Member;
import org.prgrms.be.app.domain.member.repository.MemberRepository;
import org.prgrms.be.app.domain.order.Order;
import org.prgrms.be.app.domain.order.repository.OrderRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    MemberRepository memberRepository;
    @InjectMocks
    MemberService memberService;

    @Test
    void 모든_멤버_조회() {
        List<Member> members = Arrays.asList(Member.create("test1", new Address("hi", "hell0", 4321)),
                Member.create("test2", new Address("hwee", "hell0", 4331)));
        when(memberRepository.getAllMember()).thenReturn(members);

        List<Member> allMember = memberService.getAllMember();
        verify(memberRepository).getAllMember();

        assertThat(allMember.size(), is(2));
        Assertions.assertThat(allMember).filteredOn(members::contains);
    }

    @Test
    void 이름으로_멤버_조회() {
        List<Member> members = Arrays.asList(Member.create("test1", new Address("hi", "hell0", 4321)),
                Member.create("test1", new Address("hwee", "hell0", 4331)));
        when(memberRepository.findMembersByName(members.get(0).getName())).thenReturn(members);

        List<Member> membersByName = memberService.findMembersByName(members.get(0).getName());
        verify(memberRepository).findMembersByName(members.get(0).getName());

        assertThat(membersByName.size(), is(2));
        Assertions.assertThat(membersByName).filteredOn(members::contains);
    }

    @Test
    void 멤버ID로_조회() {
        Member member = Member.create("test1", new Address("hi", "hell0", 4321));

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        Member memberById = memberService.findMemberById(member.getId());
        verify(memberRepository).findById(member.getId());

        assertThat(memberById, samePropertyValuesAs(member));
    }

    @Test
    void 멤버ID로_삭제() {
        Member member = Member.create("test1", new Address("hi", "hell0", 4321));
        doReturn(Optional.of(member)).when(memberRepository).findById(member.getId());
        when(memberRepository.delete(member.getId())).thenReturn(member.getId());

        UUID actualId = memberService.deleteMember(member.getId());
        verify(memberRepository).findById(member.getId());
        verify(memberRepository).delete(member.getId());

        assertThat(actualId, is(member.getId()));
    }

    @Test
    void 멤버_Address_수정하기() {
        Member member = Member.create("test1", new Address("hi", "hell0", 4321));
        Address newAddress = new Address("daegu", "36-1", 1234);
        doReturn(Optional.of(member)).when(memberRepository).findById(member.getId());

        member.setAddress(newAddress);
        doReturn(member).when(memberRepository).update(member);

        Member actualMember = memberService.updateAddress(member.getId(), newAddress);
        verify(memberRepository).findById(member.getId());
        verify(memberRepository).update(member);

        assertThat(actualMember.getAddress(), samePropertyValuesAs(newAddress));
    }

    @Test
    void 주문내역_조회하기() {
        UUID uuid = UUID.randomUUID();
        List<Order> orders = Arrays.asList(Order.create(uuid, new Address("hi", "hell0", 4321)),
                Order.create(uuid, new Address("ij", "hel320", 43215)));
        doReturn(orders).when(orderRepository).findByMemberId(uuid);

        List<Order> actualOrders = memberService.orderHistoryByMemberId(uuid);
        verify(orderRepository).findByMemberId(uuid);

        assertThat(actualOrders.size(), is(2));
    }
}