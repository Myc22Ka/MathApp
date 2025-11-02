package pl.myc22ka.mathapp.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.myc22ka.mathapp.user.component.helper.UserHelper;
import pl.myc22ka.mathapp.user.model.Role;
import pl.myc22ka.mathapp.user.model.User;
import pl.myc22ka.mathapp.user.repository.UserRepository;

import java.util.List;

/**
 * Service for managing users.
 * Provides operations for fetching users and updating their roles.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserHelper userHelper;

    /**
     * Returns all users.
     * Intended for admin use.
     *
     * @return list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Updates a user's role.
     *
     * @param userId the ID of the user
     * @param newRole the new role to assign
     * @return the updated user
     */
    @Transactional
    public User updateUserRole(Long userId, Role newRole) {
        User user = userHelper.getUserById(userId);
        user.setRole(newRole);
        return userRepository.save(user);
    }
}
