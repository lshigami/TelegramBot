package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = "SELECT * FROM user ORDER BY points DESC LIMIT 100", nativeQuery = true)
    List<User> findTop100UsersByPoints();
    Optional<User> findByIdAndUserGroupsContaining(String id, String group);
}
