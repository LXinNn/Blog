package com.niu.controller;

import com.niu.pojo.Category;
import com.niu.pojo.Post;
import com.niu.pojo.Tag;
import com.niu.pojo.User;
import com.niu.repository.*;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
    @Resource
    PostRepository postRepository;
    @Resource
    CommentRepository commentRepository;
    @Resource
    CategoryRepository categoryRepository;
    @Resource
    UserRepository userRepository;
    @Resource
    RoleRepository roleRepository;
    @Resource
    TagRepository tagRepository;
    @RequiresRoles("admin")
    @RequestMapping("/toAdmin")
    public String toAdmin(Model model){
        List<Post> posts = postRepository.findAll();
        long userNum = userRepository.count();
        int postNum = posts.size();
        int viewNum = 0;
        for (Post post: posts) {
            viewNum += post.getView();
        }
        long commentNum = commentRepository.count();
        model.addAttribute("userNum",userNum);
        model.addAttribute("postNum",postNum);
        model.addAttribute("viewNum",viewNum);
        model.addAttribute("commentNum",commentNum);
        return "admin/admin";
    }
    @RequiresRoles("admin")
    @RequestMapping("/toAdminCate")
    public String toAdminCate(Model model){
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories",categories);
        return "admin/blog-category";
    }
    @RequiresRoles("admin")
    @RequestMapping("/toAdminTag")
    public String toAdminTag(Model model){
        List<Tag> tags = tagRepository.findAll();
        model.addAttribute("tags",tags);
        return "admin/blog-tag";
    }
    @RequiresRoles("admin")
    @RequestMapping("/toAdminUser")
    public String toAdminUser(Model model){
        List<User> users = userRepository.findAll();
        Map<User,int[]> usersInfo = new HashMap<>();
        for(User user:users){
            if(user.getRoles().contains(roleRepository.getById(1))){continue;}
            usersInfo.put(user,new int[]{postRepository.findByAuthor(user.getName()).size(),commentRepository.findByName(user.getName()).size()});
        }
        model.addAttribute("usersinfo",usersInfo);
        return "admin/user";
    }
    @RequiresRoles("admin")
    @RequestMapping("/addCate")
    public String addCate(Category category){
        categoryRepository.save(category);
        return "redirect:/toAdminCate";
    }
    @RequiresRoles("admin")
    @RequestMapping("/deleteCate/{id}/")
    public String deleteCate(@PathVariable("id") Integer id){
        categoryRepository.deleteById(id);
        return "redirect:/toAdminCate";
    }
    @RequiresRoles("admin")
    @RequestMapping("/addTag")
    public String addTag(Tag tag){
        tagRepository.save(tag);
        return "redirect:/toAdminTag";
    }
    @RequiresRoles("admin")
    @RequestMapping("/deleteTag/{id}/")
    public String deleteTag(@PathVariable("id") Integer id){
        tagRepository.deleteById(id);
        return "redirect:/toAdminTag";
    }
    @RequiresRoles("admin")
    @RequestMapping("/deleteUser/{id}/")
    public String deleteUser(@PathVariable("id") Integer id){
        userRepository.deleteById(id);
        return "redirect:/toAdminUser";
    }
}
