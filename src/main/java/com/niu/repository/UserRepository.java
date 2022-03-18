package com.niu.repository;

import com.niu.pojo.Role;
import com.niu.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByNameAndPassword(String name,String password);

    User findByName(String name);

}
