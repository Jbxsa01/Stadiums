package org.example.usersservice.service;

import org.example.usersservice.entity.User;
import org.example.usersservice.enums.Role;
import org.example.usersservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {  // ← AJOUTEZ "implements UserService"

    private final UserRepository userRepo;

    @Override
    public User addUser(User user, Role role) {
        user.setRole(role);
        return userRepo.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Override
    public User getById(Long id) {
        return userRepo.findById(id).orElse(null);
    }
}