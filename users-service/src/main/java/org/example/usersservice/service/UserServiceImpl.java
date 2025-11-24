package org.example.usersservice.service;

import org.example.usersservice.entity.User;
import org.example.usersservice.enums.Role;
import org.example.usersservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepo;

    public User addUser(User user, Role role) {
        user.setRole(role);
        return userRepo.save(user);
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public User getById(Long id) {
        return userRepo.findById(id).orElse(null);
    }
}