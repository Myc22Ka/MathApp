package pl.myc22ka.mathapp.user.component.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.myc22ka.mathapp.exceptions.custom.UserException;
import pl.myc22ka.mathapp.user.model.Role;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.repository.UserRepository;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Helper class for managing User entities.
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@Component
@RequiredArgsConstructor
public class UserHelper {

    private final UserRepository userRepository;

    /**
     * Retrieves a user by email.
     *
     * @param email the email of the user
     * @return the User entity
     * @throws UsernameNotFoundException if no user with the given email exists
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user
     * @return the User entity
     * @throws UsernameNotFoundException if no user with the given ID exists
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with id: " + id + " doesn't exist"));
    }

    /**
     * Saves a user to the database.
     *
     * @param user the User entity to save
     */
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * Creates a new OAuth2 user if one with the given email does not exist.
     * Generates a unique login based on the email prefix.
     *
     * @param email     the email from the OAuth2 provider
     * @param oauthUser the OAuth2 user object
     * @return the existing or newly created User entity
     */
    @Transactional
    public User createOAuth2User(String email, DefaultOAuth2User oauthUser) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            String baseLogin = email.split("@")[0];
            String uniqueLogin = generateUniqueLogin(baseLogin);

            User newUser = User.builder()
                    .email(email)
                    .login(uniqueLogin)
                    .firstname(oauthUser.getAttribute("given_name"))
                    .lastname(oauthUser.getAttribute("family_name"))
                    .password("") // OAuth users do not have a password
                    .role(Role.STUDENT)
                    .verified(true)
                    .points(0.0)
                    .level(1)
                    .dailyTasksCompleted(0)
                    .twoFactorEnabled(false)
                    .notificationsEnabled(true)
                    .build();

            return userRepository.save(newUser);
        });
    }

    /**
     * Generates a unique login string based on the given base login.
     * If a login already exists, appends a number suffix until a unique login is found.
     *
     * @param baseLogin the base login string derived from email
     * @return a unique login string
     */
    private String generateUniqueLogin(String baseLogin) {
        String login = baseLogin;
        AtomicInteger counter = new AtomicInteger(1);

        while (userRepository.findByLogin(login).isPresent()) {
            login = baseLogin + counter.getAndIncrement();
        }

        return login;
    }
}
