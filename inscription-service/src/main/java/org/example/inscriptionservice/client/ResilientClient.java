package org.example.inscriptionservice.client;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.example.inscriptionservice.dto.UserResponseWrapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
@Service
public class ResilientClient {
    private final UserClient userClient;

    public ResilientClient(UserClient userClient) {
        this.userClient = userClient;
    }

    @CircuitBreaker(name = "users-service", fallbackMethod = "getUserByIdFallback")
    @Retry (name = "users-service")
    public UserResponseWrapper getUserById(Long id){
        try {
            return userClient.getUserById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user data", e);
        }
    }
}

