package pl.myc22ka.mathapp.user.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.myc22ka.mathapp.user.model.User;

import java.time.LocalDateTime;
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

    Optional<User> findByVerificationCode(String code);

    /**
     * Resetuje lastDailyTaskDate na poprzedni interwał
     * Zamiast ustawiać na null, ustawiamy na czas która pozwala na nowe rozwiązanie
     */
    @Modifying
    @Transactional
    default void resetDailyTaskDateForAll(long intervalMinutes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime previousInterval = now.minusMinutes(intervalMinutes);

        resetDailyTaskDateForAllQuery(previousInterval);
    }

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastDailyTaskDate = :previousInterval")
    void resetDailyTaskDateForAllQuery(@Param("previousInterval") LocalDateTime previousInterval);

    /**
     * Resetuje streak użytkowników którzy nie uzupełnili zadania w czasie
     *
     * @param executionTime czas wykonania crona
     * @param intervalMinutes interwał między wykonaniami w minutach (z parsowania cron'a)
     */
    @Modifying
    @Transactional
    default void resetBrokenStreaks(@NotNull LocalDateTime executionTime, long intervalMinutes) {
        LocalDateTime expectedDeadline = executionTime.minusMinutes(intervalMinutes);

        resetBrokenStreaksQuery(expectedDeadline);
    }

    @Modifying
    @Transactional
    @Query("""
            UPDATE User u
            SET u.streak = 0
            WHERE u.lastDailyTaskDate IS NULL
               OR u.lastDailyTaskDate < :expectedDeadline
        """)
    void resetBrokenStreaksQuery(@Param("expectedDeadline") LocalDateTime expectedDeadline);
}
