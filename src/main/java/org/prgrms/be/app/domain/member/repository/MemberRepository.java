package org.prgrms.be.app.domain.member.repository;

import org.prgrms.be.app.domain.member.Member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository {
    Member insert(Member member);

    UUID delete(UUID memberId);

    Member update(Member member);

    Optional<Member> findById(UUID memberId);

    List<Member> getAllMember();

    List<Member> findMembersByName(String name);

    void clear();
}
