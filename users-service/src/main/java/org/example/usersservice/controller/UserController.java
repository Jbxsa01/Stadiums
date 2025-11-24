package org.example.usersservice.controller;

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
}