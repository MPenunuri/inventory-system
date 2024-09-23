package com.mapera.inventory_system.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserCrudRepository;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

@Component
public class GenerateAdminUser {

    @Autowired
    UserCrudRepository userCrudRepository;

    @Value("${spring.r2dbc.username}")
    private String username;

    @Value("${spring.r2dbc.password}")
    private String password;

    public Mono<Void> registerAdmin(String email, String psw) {
        UserEntity adminEntity = new UserEntity();
        adminEntity.setUsername("Admin");
        adminEntity.setEmail(email);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        adminEntity.setPassword(passwordEncoder.encode(psw));
        adminEntity.setRoles("ADMIN");
        return userCrudRepository.save(adminEntity).then();
    }

    @PostConstruct
    public void init() {
        userCrudRepository.findUserByEmail(username)
                .flatMap(existingUser -> Mono.empty())
                .switchIfEmpty(registerAdmin(username, password))
                .subscribe();
    }
}
