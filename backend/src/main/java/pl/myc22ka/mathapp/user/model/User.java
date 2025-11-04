package pl.myc22ka.mathapp.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.myc22ka.mathapp.level.model.LevelRequirement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a user in the system.
 * <p>
 * Stores login info, personal details, role, gamification data (points, level, tasks),
 * and settings like email verification, 2FA, and notifications.
 * Implements UserDetails for Spring Security.
 *
 * @author Myc22Ka
 * @version 1.2.1
 * @since 01.11.2025
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    private String firstname;
    private String lastname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // ====== ELEMENTY GRYWALIZACJI ======
    @Column(nullable = false)
    private Double points = 0.0;

    @Column(nullable = false)
    private Integer level = 1;

    private LocalDate lastDailyTaskDate;

    @Column(nullable = false)
    private Integer dailyTasksCompleted = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserExercise> userExercises = new ArrayList<>();

    // ====== VERIFICATION ======
    @Column(nullable = false)
    private Boolean verified = false;

    @Column(nullable = false)
    private Boolean twoFactorEnabled = false;

    @Column(nullable = false)
    private Boolean notificationsEnabled = true;

    private String verificationCode;
    private LocalDateTime verificationCodeExpiresAt;

    // ====== DANE DODATKOWE ======
    @Column(name = "phone_number")
    private String phoneNumber;

    private String address;

    private LocalDate dateOfBirth;

    private String gender;

    // ====== SPRING SECURITY ======
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Checks if the user's email is verified.
     *
     * @return true if verified, false otherwise
     */
    public boolean isVerified() {
        return verified;
    }

    // ======== ELEMENTY GRYWALIZACJI ==========
    /**
     * Adds points to the user and updates their level if requirements are met.
     *
     * @param additionalPoints  points to add
     * @param levelRequirements list of level thresholds
     */
    public void addPoints(Double additionalPoints, @NotNull List<LevelRequirement> levelRequirements) {
        this.points += additionalPoints;

        int newLevel = levelRequirements.stream()
                .filter(req -> this.points >= req.getRequiredXp())
                .mapToInt(LevelRequirement::getLevel)
                .max()
                .orElse(this.level);

        if (newLevel > this.level) {
            this.level = newLevel;
        }
    }
}
