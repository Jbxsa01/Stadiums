package org.example.usersservice.service;

import org.example.usersservice.entity.Role;
import org.example.usersservice.entity.User;
import org.example.usersservice.repository.RoleRepository;
import org.example.usersservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    public User saveUser(User user, String roleName) {
        Role role = roleRepo.findByRoleName(roleName);
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

