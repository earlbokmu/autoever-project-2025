package com.project.autoever.repository;

import com.project.autoever.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByAccount(String account);
    
    boolean existsByResidentNumber(String residentNumber);
    
    Optional<User> findByAccount(String account);
    
    void deleteByAccount(String account);
}
