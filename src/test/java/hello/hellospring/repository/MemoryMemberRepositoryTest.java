package hello.hellospring.repository;

import static org.assertj.core.api.Assertions.assertThat;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class MemoryMemberRepositoryTest {
    private MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    public void save() {
        final Member member = new Member();
        member.setName("spring");
        repository.save(member);

        final Member result = repository.findById(member.getId()).get();
        assertThat(result).isEqualTo(member);
    }

    @Test
    public void findByName() {
        final Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        final Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();
        assertThat(result).isEqualTo(member1);

        result = repository.findByName("spring2").get();
        assertThat(result).isEqualTo(member2);
    }

    @Test
    public void findAll() {
        final Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        final Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        assertThat(repository.findAll()).hasSize(2);
    }

}