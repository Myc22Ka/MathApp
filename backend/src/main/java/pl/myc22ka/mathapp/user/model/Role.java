package pl.myc22ka.mathapp.user.model;

/**
 * Enum representing the possible roles a user can have in the system.
 * Used to manage permissions, access levels, and visibility of features.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 01.11.2025
 */
public enum Role {
    /**
     * Administrator with full access to all resources.
     */
    ADMIN,

    /**
     * Student user, typically limited to learning and exercises.
     */
    STUDENT,

    /**
     * Teacher user, can manage courses and students.
     */
    TEACHER,

    /**
     * Moderator with limited management permissions.
     */
    MODERATOR,

    /**
     * Guest user with minimal access and no persistent data.
     */
    GUEST
}