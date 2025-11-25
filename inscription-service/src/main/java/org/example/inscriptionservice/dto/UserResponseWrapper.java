package org.example.inscriptionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseWrapper {
    private boolean success;
    private String message;
    private UserResponse data;
}