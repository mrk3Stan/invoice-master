package dev.mrk3stan.invoicemaster.repository.implementation;

import dev.mrk3stan.invoicemaster.domain.Role;
import dev.mrk3stan.invoicemaster.domain.User;
import dev.mrk3stan.invoicemaster.exception.ApiException;
import dev.mrk3stan.invoicemaster.repository.RoleRepository;
import dev.mrk3stan.invoicemaster.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static dev.mrk3stan.invoicemaster.enumeration.VerificationType.ACCOUNT;
import static dev.mrk3stan.invoicemaster.query.UserQuery.*;
import static dev.mrk3stan.invoicemaster.enumeration.RoleType.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User> {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RoleRepository<Role> roleRepository;
    private final BCryptPasswordEncoder encoder;


    @Override
    public User create(User user) {
        // check the email is unique
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0) throw new ApiException("Email already in use. Please use a different email and try again.");
        // save new user
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            SqlParameterSource sqlParameterSource = getSqlParameterSource(user);
            jdbcTemplate.update(INSERT_USER_QUERY, sqlParameterSource, keyHolder);
            user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
            // add role to the user
            roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());
            // send verification url
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            // save url in verification table
            jdbcTemplate.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, Map.of(
                    "userId", user.getId(),
                    "url", verificationUrl)
            );
            // send email to user with verification url
//            emailService.sendVerificationUrl(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT);
            user.setEnabled(false);
            user.setNotLocked(true);
            // return the newly created user
            return user;
            // if any errors, throw exception with proper message
        } catch (Exception exception) {
            throw new ApiException("An error occurred. Please try again.");
        }
    }


    @Override
    public Collection<User> list(int page, int pageSize) {
        return null;
    }

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public User update(User data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    private Integer getEmailCount(String email) {
        return jdbcTemplate.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", encoder.encode(user.getPassword()));
    }

    private String getVerificationUrl(String key, String type) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + type + "/" + key).toUriString();
    }
}
