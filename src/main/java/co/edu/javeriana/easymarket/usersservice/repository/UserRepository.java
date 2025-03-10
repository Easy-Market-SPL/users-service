package co.edu.javeriana.easymarket.usersservice.repository;

import co.edu.javeriana.easymarket.usersservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE " +
            "(:fullname IS NULL OR u.fullname LIKE %:fullname%) AND " +
            "(:username IS NULL OR u.username LIKE %:username%) AND " +
            "(:email IS NULL OR u.email LIKE %:email%) AND " +
            "(:rol IS NULL OR u.rol = :rol) AND" +
            "(:deleted IS NULL OR u.deleted = :deleted)")
    List<User> findUsersByFilters(String fullname, String username, String email, String rol, Boolean deleted);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

}