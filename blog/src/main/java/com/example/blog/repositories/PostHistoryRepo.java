package com.example.blog.repositories;

import com.example.blog.entities.PostHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostHistoryRepo extends JpaRepository<PostHistory,Integer> {
    PostHistory findByPostIdAndUserId(Integer postId,Integer userId);

    Void deleteByPostIdAndUserIdAndType(Integer postId, Integer userId, String type);

    Optional<PostHistory> findByPostIdAndUserIdAndType(Integer postId, Integer userId, PostHistory.Type type);

    Boolean existsByPostIdAndUserIdAndType(Integer postId, Integer userId, PostHistory.Type type);
}
