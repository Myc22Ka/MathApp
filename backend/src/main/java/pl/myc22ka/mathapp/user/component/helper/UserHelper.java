package pl.myc22ka.mathapp.user.component.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.exceptions.custom.UserException;
import pl.myc22ka.mathapp.user.model.Role;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserHelper {

    private final UserRepository userRepository;

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User does not exist"));
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("User with id: " + id + " doesn't exist"));
    }

    public void save(User user){
        userRepository.save(user);
    }

    public User createOAuth2User(String email, DefaultOAuth2User oauthUser) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .login(email.split("@")[0])
                    .firstname(oauthUser.getAttribute("given_name"))
                    .lastname(oauthUser.getAttribute("family_name"))
                    .password("")
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
}
