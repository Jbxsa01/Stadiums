
package org.example.usersservice.service;

import org.example.usersservice.entity.User;
import org.example.usersservice.enums.Role;

import java.util.List;

public interface UserService {
    User addUser(User user, Role role);
    List<User> getAll();
    User getById(Long id);
}

