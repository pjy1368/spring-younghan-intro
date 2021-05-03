package hello.hellospring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberServiceTest {

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(new MemoryMemberRepository());
    }

    @Test
    void 회원가입() {
        // given
        final Member member = new Member();
        member.setName("hello");

        // when
        final long savedId = memberService.join(member);

        // then
        assertThat(memberService.findOne(savedId).get().getName()).isEqualTo(member.getName());
    }

    @Test
    void 중복_회원_예외() {
        // given
        final Member member1 = new Member();
        member1.setName("spring1");

        final Member member2 = new Member();
        member2.setName("spring1");
        // when
        memberService.join(member1);

        // then
        assertThatThrownBy(() -> memberService.join(member2))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("이미 존재하는 회원입니다.");
    }

}