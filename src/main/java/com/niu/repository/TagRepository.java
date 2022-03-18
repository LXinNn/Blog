package com.niu.repository;

import com.niu.pojo.Post;
import com.niu.pojo.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Integer> {
    Tag findByText(String text);
}
