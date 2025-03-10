package co.edu.javeriana.easymarket.usersservice.services;

import co.edu.javeriana.easymarket.usersservice.model.User;
import co.edu.javeriana.easymarket.usersservice.repository.UserRepository;
import co.edu.javeriana.easymarket.usersservice.utils.OperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all not deleted users
    public List<User> getUsers(String fullname, String username, String email, String rol, Boolean deleted) {
        if(deleted == null)
            deleted = false;
        return userRepository.findUsersByFilters(fullname, username, email, rol, deleted);
    }

    // Get a user by id
    public User getUser(String id) throws OperationException {
        User user = userRepository.findById(id).orElse(null);

        if (user == null)
            throw new OperationException(404, "User not found");

        if (user.getDeleted())
            throw new OperationException(404, "User account is deleted");

        return user;
    }

    // Create a new user
    public User createUser(User user) throws OperationException {
        // Check if the user already exists
        if (userRepository.findById(user.getId()).isPresent()) {
            throw new OperationException(400, "User already exists");
        }

        // Check if valid email and username are provided
        if (user.getEmail() != null && userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new OperationException(400, "Email already in use");
        }

        if (user.getUsername() != null && userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new OperationException(400, "Username already in use");
        }

        user.setDeleted(false);

        try {
            return userRepository.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new OperationException(500, "User could not be created");
        }
    }

    // Update an existing user
    public User updateUser(String id, User user) throws OperationException {
        User updatedUser = userRepository.findById(id).orElse(null);

        // Check if the user exists
        if (updatedUser == null)
            throw new OperationException(404, "User not found");

        // Check if valid email and username are provided
        if (user.getEmail() != null && !user.getEmail().equals(updatedUser.getEmail()) && userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new OperationException(400, "Email already in use");
        }

        if (user.getUsername() != null && !user.getUsername().equals(updatedUser.getUsername()) && userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new OperationException(400, "Username already in use");
        }

        // Update user
        if (user.getEmail() != null)
            updatedUser.setEmail(user.getEmail());

        if (user.getUsername() != null)
            updatedUser.setUsername(user.getUsername());

        if(user.getFullname() != null)
            updatedUser.setFullname(user.getFullname());

        if(user.getRol() != null)
            updatedUser.setRol(user.getRol());

        return userRepository.save(updatedUser);
    }

    // Delete a user
    public User deleteUser(String id) throws OperationException {
        User user = userRepository.findById(id).orElse(null);

        if (user == null)
            throw new OperationException(404, "User not found");

        if (user.getDeleted())
            throw new OperationException(404, "User account is already deleted");

        user.setDeleted(true);
        return userRepository.save(user);
    }

    // Restore a deleted user
    public User restoreUser(String id) throws OperationException {
        User user = userRepository.findById(id).orElse(null);

        if (user == null)
            throw new OperationException(404, "User not found");

        if(user.getDeleted()) {
            user.setDeleted(false);
            return userRepository.save(user);
        }
        return user;
    }

    // Delete a user permanently
    public void deletePermanentlyUser(String id) throws OperationException {
        User user = userRepository.findById(id).orElse(null);

        if (user == null)
            throw new OperationException(404, "User not found");

        userRepository.delete(user);
    }

}
