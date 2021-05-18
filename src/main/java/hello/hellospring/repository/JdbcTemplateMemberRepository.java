package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class JdbcTemplateMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateMemberRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        final SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());

        final Number key = jdbcInsert.executeAndReturnKey(new
            MapSqlParameterSource(parameters));
        member.setId(key.longValue());
        return member;
    }

    @Override
    public Optional<Member> findById(long id) {
        final String sql = "select * from member where id = ?";
        final List<Member> result = jdbcTemplate.query(sql, memberRowMapper(), id);
        return Optional.ofNullable(DataAccessUtils.singleResult(result));
    }

    @Override
    public Optional<Member> findByName(String name) {
        final String sql = "select * from member where name = ?";
        final List<Member> result = jdbcTemplate.query(sql, memberRowMapper(), name);
        return Optional.ofNullable(DataAccessUtils.singleResult(result));
    }

    @Override
    public List<Member> findAll() {
        final String sql = "select * from member";
        return jdbcTemplate.query(sql, memberRowMapper());
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));
            return member;
        };
    }
}
