package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.mapera.inventory_system.application.port.outbound.UserPersistencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;

import reactor.core.publisher.Mono;

public class UserRepositoryImpl
        implements UserRepositoryCustom, UserPersistencePort {

    @Autowired
    UserCrudRepository userCrudRepository;

    public Mono<Boolean> registeredEmail(String email) {
        return userCrudRepository.findUserByEmail(email)
                .flatMap(existingUser -> Mono.just(true))
                .switchIfEmpty(Mono.just(false));
    };

    @Override
    public Mono<UserEntity> registerUser(String name, String email, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(name);
        userEntity.setEmail(email);
        userEntity.setPassword(password);
        userEntity.setRoles("USER");
        return registeredEmail(email).flatMap(registered -> {
            if (registered) {
                throw new IllegalArgumentException("Cannot register an email that is already registered");
            }
            return userCrudRepository.save(userEntity);
        });
    }

    @Override
    public Mono<UserEntity> findUserById(Long id) {
        return userCrudRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }

    @Override
    public Mono<UserEntity> findUserByEmail(String email) {
        return userCrudRepository.findUserByEmail(email)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }

    @Override
    public Mono<UserEntity> updateUserName(Long id, String name) {
        return userCrudRepository.findById(id).flatMap(user -> {
            user.setUsername(name);
            return userCrudRepository.save(user)
                    .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
        });
    }

    @Override
    public Mono<UserEntity> updateUserEmail(Long id, String email) {
        return userCrudRepository.findById(id).flatMap(user -> {
            user.setEmail(email);
            return userCrudRepository.save(user)
                    .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
        });
    }

    @Override
    public Mono<UserEntity> updateUserPassword(Long id, String password) {
        return userCrudRepository.findById(id).flatMap(user -> {
            user.setPassword(password);
            return userCrudRepository.save(user)
                    .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
        });
    }

    @Override
    public Mono<Void> deleteUserById(Long id) {
        return userCrudRepository.deleteById(id)
                .onErrorMap(error -> {
                    if (error instanceof DataIntegrityViolationException) {
                        return new IllegalStateException(
                                "Failed to delete user with ID: " + id + ". " +
                                        "The user is associated with other records and cannot be deleted. "
                                        +
                                        "Please remove any related registry before attempting to delete this user.",
                                error);
                    }
                    return new IllegalArgumentException(
                            "Failed to delete user with ID: " + id + ". Unexpected error occurred.");
                });
    }

}
