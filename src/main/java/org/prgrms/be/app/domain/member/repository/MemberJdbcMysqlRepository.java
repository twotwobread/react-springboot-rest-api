package org.prgrms.be.app.domain.member.repository;

import org.prgrms.be.app.domain.Address;
import org.prgrms.be.app.domain.member.Member;
import org.prgrms.be.app.exception.DatabaseFindException;
import org.prgrms.be.app.exception.DatabaseUpdateException;
import org.prgrms.be.app.util.ConvertUtil;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class MemberJdbcMysqlRepository implements MemberRepository {

    private final static RowMapper<Member> memberRowMapper = (resultSet, i) -> mapToMember(resultSet);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MemberJdbcMysqlRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Member mapToMember(ResultSet resultSet) throws SQLException {
        UUID memberId = ConvertUtil.toUUID(resultSet.getBytes("member_id"));
        String name = resultSet.getString("name");
        String address = resultSet.getString("address");
        String addressDetails = resultSet.getString("address_details");
        int zipcode = resultSet.getInt("zipcode");
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

        return new Member(memberId, name, new Address(address, addressDetails, zipcode), createdAt);
    }

    @Override
    public Member insert(Member member) {
        int insertCnt = jdbcTemplate.update("INSERT INTO member(member_id, name, address, address_details, zipcode, created_at)" +
                        " VALUES(UNHEX(REPLACE(:memberId,'-','')), :name, :address, :addressDetails, :zipcode, :createdAt)",
                toParamMap(member));

        if (insertCnt != 1) {
            throw new DatabaseUpdateException("member: insert가 수행되지 않았습니다.");
        }

        return member;
    }

    @Override
    public UUID delete(UUID memberId) {
        int deleteCnt = jdbcTemplate.update("DELETE FROM member WHERE member_id = UNHEX(REPLACE(:memberId, '-', ''))",
                Collections.singletonMap("memberId", memberId.toString()));

        if (deleteCnt != 1) {
            throw new DatabaseUpdateException("member: delete가 수행되지 않았습니다.");
        }

        return memberId;
    }

    @Override
    public Member update(Member member) {
        int updateCnt = jdbcTemplate.update("UPDATE member SET name=:name, address=:address, address_details=:addressDetails, zipcode=:zipcode WHERE member_id=UNHEX(REPLACE(:memberId,'-',''))",
                toParamMap(member));

        if (updateCnt != 1) {
            throw new DatabaseUpdateException("member: update가 수행되지 않았습니다.");
        }

        return member;
    }

    @Override
    public Optional<Member> findById(UUID memberId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM member WHERE member_id = UNHEX(REPLACE(:memberId,'-',''))",
                    Collections.singletonMap("memberId", memberId.toString()),
                    memberRowMapper));
        } catch (EmptyResultDataAccessException e) {
            throw new DatabaseFindException("member: 해당 id를 가진 member가 존재하지 않습니다.", e);
        }
    }

    @Override
    public List<Member> getAllMember() {
        return jdbcTemplate.query("SELECT * FROM member",
                Collections.emptyMap(),
                memberRowMapper);
    }

    @Override
    public List<Member> findMembersByName(String name) {
        return jdbcTemplate.query("SELECT * FROM member WHERE name=:name",
                Collections.singletonMap("name", name),
                memberRowMapper);
    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM member", Collections.emptyMap());
    }

    private Map<String, Object> toParamMap(Member member) {
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", member.getId().toString());
        map.put("name", member.getName());
        map.put("address", member.getAddress().getAddress());
        map.put("addressDetails", member.getAddress().getDetails());
        map.put("zipcode", member.getAddress().getZipCode());
        map.put("createdAt", member.getCreatedAt());

        return map;
    }
}
