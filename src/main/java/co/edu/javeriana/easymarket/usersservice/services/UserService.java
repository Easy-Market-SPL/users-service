package co.edu.javeriana.easymarket.usersservice.services;

import co.edu.javeriana.easymarket.usersservice.dtos.UserDTO;
import co.edu.javeriana.easymarket.usersservice.model.User;
import co.edu.javeriana.easymarket.usersservice.repository.UserRepository;
import co.edu.javeriana.easymarket.usersservice.utils.OperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create a new user
    public User createUser(User user) throws OperationException {
        // Check if the user already exists
        if (userRepository.findById(user.getId()).isPresent() ||
            userRepository.findByEmail(user.getEmail()).isPresent() ||
            userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new OperationException(400, "User already exists");
        }

        // Set the user role
        user.setRol("customer");
        user.setDeleted(false);

        try {
            return userRepository.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new OperationException(500, "User could not be created");
        }
    }

    // Get all not deleted users
    public List<User> getUsers() {
        return userRepository.findByDeletedFalse();
    }

    // Get all deleted users
    public List<User> getDeletedUsers() {
        return userRepository.findByDeletedTrue();
    }
}
