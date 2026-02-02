package com.shubham.authentication.repo;

import com.shubham.authentication.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    Optional<Users> findByUsernameAndIsActive(String username, Boolean active);
}
