package com.example.blog.repositories;

import com.example.blog.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepo extends JpaRepository<VerificationToken,Integer> {
    VerificationToken findByToken(String token);
}
