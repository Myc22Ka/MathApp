package pl.myc22ka.mathapp.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.myc22ka.mathapp.user.model.User;

import java.util.Optional;

/**
 * Repository for managing User entities.
 * <p>
 * Provides basic CRUD operations and methods to find users by email or login.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Finds a user by email.
     *
     * @param email the user's email
     * @return an optional User
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by login.
     *
     * @param login the user's login
     * @return an optional User
     */
    Optional<User> findByLogin(String login);
}
