/*package org.example.usersservice.controller;

import org.example.usersservice.entity.User;
import org.example.usersservice.enums.Role;
import org.example.usersservice.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/add")
    public User addUser(@RequestBody User user,
                        @RequestParam Role role) {
        return userService.addUser(user, role);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getById(id);
    }
}*/
package org.example.usersservice.controller;

import org.example.usersservice.entity.User;
import org.example.usersservice.enums.Role;
import org.example.usersservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addUser(@RequestBody User user, @RequestParam Role role) {
        User savedUser = userService.addUser(user, role);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Utilisateur créé avec succès");
        response.put("data", savedUser);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable Long id) {
        User user = userService.getById(id);

        Map<String, Object> response = new HashMap<>();

        if (user != null) {
            response.put("success", true);
            response.put("message", "Utilisateur trouvé");
            response.put("data", user);
            return ResponseEntity.ok(response);
        }

        response.put("success", false);
        response.put("message", "Utilisateur non trouvé");
        response.put("data", null);
        return ResponseEntity.status(404).body(response);
    }
}