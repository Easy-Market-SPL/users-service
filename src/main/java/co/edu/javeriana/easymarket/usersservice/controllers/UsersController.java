package co.edu.javeriana.easymarket.usersservice.controllers;

import co.edu.javeriana.easymarket.usersservice.dtos.Response;
import co.edu.javeriana.easymarket.usersservice.dtos.UserDTO;
import co.edu.javeriana.easymarket.usersservice.mappers.UserMapper;
import co.edu.javeriana.easymarket.usersservice.model.User;
import co.edu.javeriana.easymarket.usersservice.services.UserService;
import co.edu.javeriana.easymarket.usersservice.utils.OperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UsersController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "rol", required = false) String rol,
            @RequestParam(value = "deleted", required = false, defaultValue = "false") boolean deleted
    ) {
        List<UserDTO> users = userService.getUsers(fullname, username, email,rol, deleted).stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        try {
            User user = userService.getUser(id);
            return ResponseEntity.ok(userMapper.userToUserDTO(user));
        } catch (OperationException e) {
            return ResponseEntity.status(e.getCode()).body(new Response(e.getCode(),e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        try {
            User user = userMapper.userDTOToUser(userDTO);
            user = userService.createUser(user);
            return ResponseEntity.ok(userMapper.userToUserDTO(user));
        } catch (OperationException e) {
            return ResponseEntity.status(e.getCode()).body(new Response(e.getCode(),e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        try {
            User user = userMapper.userDTOToUser(userDTO);
            user = userService.updateUser(id, user);
            return ResponseEntity.ok(userMapper.userToUserDTO(user));
        } catch (OperationException e) {
            return ResponseEntity.status(e.getCode()).body(new Response(e.getCode(),e.getMessage()));
        }
    }

    @PutMapping("/{id}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            User user = userService.deleteUser(id);
            return ResponseEntity.ok(userMapper.userToUserDTO(user));
        } catch (OperationException e) {
            return ResponseEntity.status(e.getCode()).body(new Response(e.getCode(),e.getMessage()));
        }
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restoreUser(@PathVariable String id) {
        try {
            User user = userService.restoreUser(id);
            return ResponseEntity.ok(userMapper.userToUserDTO(user));
        } catch (OperationException e) {
            return ResponseEntity.status(e.getCode()).body(new Response(e.getCode(),e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserPermanently(@PathVariable String id) {
        userService.deletePermanentlyUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
