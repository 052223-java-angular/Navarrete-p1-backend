package com.revature.movietn.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.revature.movietn.dtos.responses.Principal;
import com.revature.movietn.entities.Role;
import com.revature.movietn.entities.User;
import com.revature.movietn.repositories.UserRepository;
import com.revature.movietn.utils.custom_exceptions.ResourceNotFoundException;
import com.revature.movietn.utils.custom_exceptions.UnauthorizedAccessException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    /**
     * Finds a user by a user id.
     * 
     * @param id the user id
     * @return the Principal object containing the user information
     */
    public Principal findById(String id) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            return new Principal(foundUser.get());
        }

        throw new ResourceNotFoundException("User not found.");
    }

    /**
     * Finds a user by username.
     * 
     * @param username the username
     * @return the Principal object containing user information.
     */
    public Principal findByUsername(String username) {
        Optional<User> foundUser = userRepository.findByUsername(username);
        if (foundUser.isPresent()) {
            return new Principal(foundUser.get());
        }

        throw new ResourceNotFoundException("User not found.");
    }

    /**
     * Creates a new user with the given username and password credentials.
     * 
     * @param username the user's username
     * @param password the user's password
     * @return the new registers User object
     */
    public Principal register(String username, String email, String password) {
        // find role
        Role role = roleService.findByName("USER");

        // hash password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // create new user
        User user = new User(username, email, hashedPassword, role);

        // save user
        return new Principal(userRepository.save(user));
    }

    /**
     * Validates user credentials and once everything checks the Principal DTO
     * is returned.
     * 
     * @param username the user's username
     * @param password the user's password
     * @return the Prinical DTO object
     */
    public Principal login(String username, String password) {
        Optional<User> foundUser = userRepository.findByUsername(username);
        if (foundUser.isPresent()) {
            if (BCrypt.checkpw(password, foundUser.get().getPassword())) {
                return new Principal(foundUser.get());
            }
        }

        throw new UnauthorizedAccessException("Invalid username or password.");
    }

    /**
     * Checks if the username is valid. A valid username must be between 8 - 20
     * characters in length. A valid username must not have '_' or '.' at the
     * beginning or end. A valid username must not have '__', '_.', '._' or '..'.
     * Allowed characers are [a-zA-z0-9._].
     * 
     * @param username the username to validate
     * @return true if username is valid, false otherwise
     */
    public boolean isValidUsername(String username) {
        return username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
    }

    /**
     * Checks against the database to see if a user with given username is in the
     * database.
     * 
     * @param username the username to check against the database
     * @return true if user with given username is not found in db, otherwise false
     */
    public boolean isUniqueUsername(String username) {
        Optional<User> foundUser = userRepository.findByUsername(username);
        if (foundUser.isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * Checks if email address is valid.
     * 
     * @param email the email to validate
     * @return true if email is valid, false otherwise.
     */
    public boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    /**
     * Checks if the password is valid. A valid password must at least 8 characters
     * long (no constraints on max length). A valid password must have at least one
     * digit, lowercase letter, uppercase letter, and special character
     * ([@#$%^&+=]).A valid password must not have any whitespaces.
     * 
     * @param password the password name to validate
     * @return true if password is valid, otherwise false
     */
    public boolean isValidPassword(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    }

    /**
     * Checks if password and password confirmation match.
     * 
     * @param password        the initial password
     * @param confirmPassword the confirmation password
     * @return true if passwords match, otherwise false
     */
    public boolean isSamePassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

}
