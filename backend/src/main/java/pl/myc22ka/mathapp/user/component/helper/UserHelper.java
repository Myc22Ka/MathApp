package pl.myc22ka.mathapp.user.component.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.myc22ka.mathapp.exceptions.custom.UserException;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserHelper {

    private final UserRepository userRepository;

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Użytkownik nie istnieje"));
    }
}
