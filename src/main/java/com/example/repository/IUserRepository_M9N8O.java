src/main/java/com/example/repository/IUserRepository_M9N8O.java
package com.example.repository;

import com.example.model.User_M1N2O;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository_M9N8O extends JpaRepository<User_M1N2O, UUID> {
    Optional<User_M1N2O> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
}