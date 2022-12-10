package org.prgrms.be.app.domain.member.repository;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import com.wix.mysql.config.Charset;
import com.wix.mysql.config.MysqldConfig;
import com.wix.mysql.distribution.Version;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.be.app.config.TestConfig;
import org.prgrms.be.app.domain.Address;
import org.prgrms.be.app.domain.member.Member;
import org.prgrms.be.app.exception.DatabaseFindException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {TestConfig.class, MemberJdbcMysqlRepository.class})
@ActiveProfiles("test")
class MemberJdbcMysqlRepositoryTest {
    static EmbeddedMysql embeddedMysql = embeddedMysql();
    @Autowired
    MemberRepository memberRepository;

    static EmbeddedMysql embeddedMysql() {
        MysqldConfig config = aMysqldConfig(Version.v5_7_latest)
                .withCharset(Charset.UTF8)
                .withPort(2215)
                .withUser("test", "test1234!")
                .withTimeZone("Asia/Seoul")
                .build();
        return anEmbeddedMysql(config)
                .addSchema("test-young_cm", ScriptResolver.classPathScripts("sql/schema.sql"))
                .start();
    }

    @AfterAll
    static void shutDown() {
        embeddedMysql.stop();
    }

    @AfterEach
    void reset() {
        memberRepository.clear();
    }

    @Test
    @DisplayName("MemberJdbcMysqlRepository에 Member 객체를 넣을 수 있다.")
    void member_삽입하기() {
        // given
        Member testMember = Member.create("test01", new Address("test01", "36-1", 43501));
        // when
        Member actualMember = memberRepository.insert(testMember);
        // then
        assertThat(actualMember, samePropertyValuesAs(testMember));
    }

    @Test
    @DisplayName("member Id를 이용해서 특정 member를 찾을 수 있다.")
    void member_ID로_찾기() {
        // given
        Member testMember = memberRepository.insert(Member.create("test1", new Address("test1", "16-1", 49815)));
        // when
        Optional<Member> actualMember = memberRepository.findById(testMember.getId());
        // then
        assertThat(actualMember.isPresent(), is(true));
        assertThat(actualMember.get(), samePropertyValuesAs(testMember));
    }

    @Test
    @DisplayName("member name를 이용해서 member들을 찾을 수 있다.")
    void member_이름으로_찾기() {
        // given
        Member testMember1 = memberRepository.insert(Member.create("test1", new Address("test1", "16-1", 49815)));
        Member testMember2 = memberRepository.insert(Member.create("test1", new Address("test2", "26-1", 49845)));
        // when
        List<Member> membersByName = memberRepository.findMembersByName(testMember1.getName());
        // then
        assertThat(membersByName, hasSize(2));
    }

    @Test
    @DisplayName("존재하는 모든 멤버를 찾을 수 있다.")
    void 모든_member_찾기() {
        // given
        Member testMember1 = memberRepository.insert(Member.create("test1", new Address("test1", "16-1", 49815)));
        Member testMember2 = memberRepository.insert(Member.create("test2", new Address("test2", "26-1", 49845)));
        // when
        List<Member> allMember = memberRepository.getAllMember();
        // then
        assertThat(allMember, hasSize(2));
    }

    @Test
    @DisplayName("member의 name과 address를 수정할 수 있다.")
    void member_수정하기() {
        // given
        Member testMember = memberRepository.insert(Member.create("test1", new Address("test1", "16-1", 49815)));
        // when
        testMember.setName("lee");
        Member updateMember = memberRepository.update(testMember);
        //then
        assertThat(updateMember.getName(), is("lee"));
    }

    @Test
    @DisplayName("member Id를 이용해서 특정 member를 삭제할 수 있다.")
    void member_삭제하기() {
        // given
        Member testMember = memberRepository.insert(Member.create("test1", new Address("test1", "16-1", 49815)));
        //when
        UUID deleteMemberId = memberRepository.delete(testMember.getId());
        // then
        assertThrows(DatabaseFindException.class, () -> memberRepository.findById(deleteMemberId));
    }
}