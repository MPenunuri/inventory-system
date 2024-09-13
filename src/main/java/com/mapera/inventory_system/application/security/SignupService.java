package com.mapera.inventory_system.application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mapera.inventory_system.application.port.outbound.UserPersistencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;

import reactor.core.publisher.Mono;

@Service
public class SignupService {

    @Autowired
    private UserPersistencePort userPersistencePort;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Mono<UserEntity> signup(String name, String email, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        return userPersistencePort.registerUser(name, email, encodedPassword);
    }
}
