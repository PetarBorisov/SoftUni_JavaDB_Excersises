package com.example.springdto.Repository;


import com.example.springdto.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository<User> extends JpaRepository<com.example.springdto.Entity.User, Long> {

    Optional<com.example.springdto.Entity.User> findByEmailAndPassword(String email, String password);
}
