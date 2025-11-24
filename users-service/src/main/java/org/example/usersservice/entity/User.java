package org.example.usersservice.entity;

import org.example.usersservice.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")  // ← IMPORTANT : Cette annotation
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}