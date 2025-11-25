package org.example.inscriptionservice.client;

import org.example.inscriptionservice.dto.UserResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-service", path = "/users")
public interface UserClient {

    @GetMapping("/{id}")
    UserResponseWrapper getUserById(@PathVariable("id") Long id);
}//les filles ici interface openfein