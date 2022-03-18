package com.niu.repository;

import com.niu.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    Category getByText(String categorytext);
}
