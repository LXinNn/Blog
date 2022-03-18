package com.niu;

import com.niu.config.ShiroConfig;
import com.niu.pojo.User;
import com.niu.repository.CategoryRepository;
import com.niu.repository.PostRepository;
import com.niu.repository.UserRepository;
import com.niu.util.MarkdownUtil;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.hibernate.dialect.Ingres9Dialect;
import org.junit.jupiter.api.Test;
import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class BlogApplicationTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ShiroConfig shiroConfig;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Test
    void changePassword(){
        postRepository.deleteById(1);
//        Hash hashPwd = new SimpleHash("md5","123456",null,1);
//        System.out.println(hashPwd.toString());
//        nlx.setPassword(hashPwd.toString());
//        userRepository.save(nlx);
    }
    @Test
    void test(){
        userRepository.deleteById(3);
    }
    @Test
    void testReadFile4() throws IOException {
    }
    @Test
    void contextLoads() {
    }

}
