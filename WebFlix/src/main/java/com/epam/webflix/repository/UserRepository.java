package com.epam.webflix.repository;

import com.epam.webflix.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAll();

    Optional<User> findById(String id);

    User save(User user);
}
