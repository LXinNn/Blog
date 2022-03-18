package com.niu.repository;

import com.niu.pojo.Category;
import com.niu.pojo.Post;
import com.niu.pojo.Tag;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {
    List<Post> findByAuthor(String author,Pageable pageable);
    List<Post> findByAuthor(String author);
    List<Post> findByTitleLike(String keyword, Pageable pageable);
    List<Post> findTop6ByOrderByViewDesc();
    List<Post> findTop3ByOrderByViewDesc();
    List<Post> findTop5ByOrderByIdDesc();
    List<Post> findByCategory(Category category,Pageable pageable);
    List<Post> findByTagsContains(Tag tag, Pageable pageable);
}
