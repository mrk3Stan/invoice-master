package dev.mrk3stan.invoicemaster.repository.implementation;

import dev.mrk3stan.invoicemaster.domain.Role;
import dev.mrk3stan.invoicemaster.exception.ApiException;
import dev.mrk3stan.invoicemaster.repository.RoleRepository;
import dev.mrk3stan.invoicemaster.rowmapper.RoleRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Objects;

import static dev.mrk3stan.invoicemaster.enumeration.RoleType.ROLE_USER;
import static dev.mrk3stan.invoicemaster.query.RoleQuery.*;
import static java.util.Map.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImpl implements RoleRepository<Role> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Role create(Role data) {
        return null;
    }

    @Override
    public Collection<Role> list(int page, int pageSize) {
        return null;
    }

    @Override
    public Role get(Long id) {
        return null;
    }

    @Override
    public Role update(Role data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) {
        log.info("Adding role {} to user id: {}", roleName, userId);

        try {
            Role role = jdbcTemplate.queryForObject(SELECT_ROLE_BY_NAME_QUERY, of("name", roleName), new RoleRowMapper());
            jdbcTemplate.update(INSERT_ROLE_TO_USER_QUERY, of("userId", userId, "roleId", Objects.requireNonNull(role).getId()));
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No role found by name: " + ROLE_USER.name());
        } catch (Exception exception) {
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public Role getRoleByUserId(Long userId) {
        return null;
    }

    @Override
    public Role getRoleByUserEmail(String email) {
        return null;
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {

    }
}
