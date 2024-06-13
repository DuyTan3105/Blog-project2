package com.example.blog.repositories;

import com.example.blog.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CommentRepo extends JpaRepository<Comment,Integer> {
    @Query("select c from Comment c where c.postId = :postId and c.parentComment is null")
    Optional<List<Comment>> findAllByPostId(@Param("postId") Integer postId);
    @Query("select c from Comment c where c.parentComment.id=:commentId")
    Optional<List<Comment>> findAllParent(@Param("commentId") Integer commentId);

}
