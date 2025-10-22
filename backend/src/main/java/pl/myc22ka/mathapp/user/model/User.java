package pl.myc22ka.mathapp.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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
    private Integer points = 0;

    @Column(nullable = false)
    private Integer level = 1;

    private LocalDate lastDailyTaskDate;

    @Column(nullable = false)
    private Integer dailyTasksCompleted = 0;

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

    public boolean isVerified() {
        return verified;
    }
}
