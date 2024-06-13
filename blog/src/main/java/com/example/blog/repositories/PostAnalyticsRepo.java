package com.example.blog.repositories;

import com.example.blog.entities.Post;
import com.example.blog.entities.Postanalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PostAnalyticsRepo extends JpaRepository<Postanalytics,Integer> {
    Optional<Postanalytics> findByPost(Post post);
}
