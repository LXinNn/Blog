package com.niu.repository;

import com.niu.pojo.Comment;
import com.niu.pojo.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findTop5ByOrderByIdDesc();
    List<Comment> findByName(String name);
}
