package co.edu.javeriana.easymarket.usersservice.repository;

import co.edu.javeriana.easymarket.usersservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByDeletedFalse();
    List<User> findByDeletedTrue();
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

}