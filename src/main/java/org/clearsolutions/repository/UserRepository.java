package org.clearsolutions.repository;

import org.clearsolutions.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findUsersByBirthDateBetween(LocalDate from, LocalDate to);
}
