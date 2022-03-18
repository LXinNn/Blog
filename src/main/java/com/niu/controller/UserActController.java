package com.niu.controller;

import com.niu.mapper.PostMapper;
import com.niu.pojo.*;
import com.niu.repository.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class UserActController {

    @Autowired
    PostMapper postMapper;
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
    @RequestMapping("/")
    public String showPost(RedirectAttributes attributes){
        int page = 1;
        attributes.addAttribute("page",page);
        return "redirect:/getPostByPage/{page}/show";
    }
    @RequestMapping("/search")
    public String search(String keyword, RedirectAttributes attributes){
        attributes.addAttribute("page",1);
        attributes.addAttribute("keyword",keyword);
        return "redirect:/searchList/{keyword}/{page}/show";
    }
    @RequestMapping("/searchList/{keyword}/{page}/show")
    public String searchList(@PathVariable("page") int page,@PathVariable("keyword")String keyword, Model model) {
        Pageable pageable = PageRequest.of(page - 1, 5);
        List<Post> posts = postRepository.findByTitleLike("%" + keyword + "%",pageable);
        List<Post> postsTop3 = postRepository.findTop3ByOrderByViewDesc();
        long totalCount = posts.size();
        long maxPage = totalCount / 5 + 1;
        boolean search = true;
        model.addAttribute("search",search);
        model.addAttribute("posts",posts);
        model.addAttribute("postsTop3",postsTop3);
        model.addAttribute("keyword",keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("maxPage", maxPage);
        model.addAttribute("totalCount", totalCount);
        return "search-list";
    }
    @RequestMapping("/toPostCate/{category}/{page}/")
    public String toCategory(@PathVariable("category")String category,Model model,@PathVariable("page") int page){
        Pageable pageable = PageRequest.of(page - 1, 5);
        Category category1 = categoryRepository.getByText(category);
        List<Post> posts = postRepository.findByCategory(category1,pageable);
        List<Post> postsTop3 = postRepository.findTop3ByOrderByViewDesc();
        long totalCount = posts.size();
        long maxPage = totalCount / 5 + 1;
        boolean iscate = true;
        model.addAttribute("cate",iscate);
        model.addAttribute("postsTop3",postsTop3);
        model.addAttribute("posts",posts);
        model.addAttribute("category",category);
        model.addAttribute("currentPage", page);
        model.addAttribute("maxPage", maxPage);
        model.addAttribute("totalCount", totalCount);
        return "search-list";
    }
    @RequestMapping("/toPostTag/{tag}/{page}/")
    public String toTag(@PathVariable("tag")String tag,Model model,@PathVariable("page") int page){
        Pageable pageable = PageRequest.of(page - 1, 5);
        Tag tag1 = tagRepository.findByText(tag);
        List<Post> posts = postRepository.findByTagsContains(tag1,pageable);
        List<Post> postsTop3 = postRepository.findTop3ByOrderByViewDesc();
        long totalCount = posts.size();
        long maxPage = totalCount / 5 + 1;
        boolean istag = true;
        model.addAttribute("istag",istag);
        model.addAttribute("postsTop3",postsTop3);
        model.addAttribute("posts",posts);
        model.addAttribute("tag",tag);
        model.addAttribute("currentPage", page);
        model.addAttribute("maxPage", maxPage);
        model.addAttribute("totalCount", totalCount);
        return "search-list";
    }
    @GetMapping("/toPost/{id}/show")
    public String toPost(Model model,@PathVariable("id") Integer id){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user!=null) {
            String username = user.getName();
            model.addAttribute("username", username);
        }
        Post post = postRepository.getById(id);
        post.setView(post.getView()+1);
        List<Post> postsTop3 = postRepository.findTop3ByOrderByViewDesc();
        long count = postRepository.count();
        postRepository.save(post);
        Set<Comment> comments = post.getComments();
        System.out.println(comments.isEmpty());
        model.addAttribute("postsTop3",postsTop3);
        model.addAttribute("comments",comments);
        model.addAttribute("post",post);
        model.addAttribute("count",count);
        return "post-view";
    }
    @RequestMapping("/login")
    public String login(String msg,Model model){
        model.addAttribute("msg",msg);
        return "sign-in";
    }
    @RequestMapping("/checkLogin")
    public String afterLogin(boolean rememberMe,String name,char[] password,RedirectAttributes attr, HttpServletRequest req){
        //获取当前的登录用户
        Subject subject = SecurityUtils.getSubject();
        //封装登录用户的用户名和密码做成UsernameToken,拿到令牌
        System.out.println(rememberMe);
        UsernamePasswordToken token = new UsernamePasswordToken(name,password,rememberMe);
        System.out.println(password);
        System.out.println(token.getPassword());
        System.out.println("**********************");
        //执行登录方法，经过一系列跳转，然后到UserRealm类的doGetAuthenticationInfo()方法，用户名和密码做认证，如果没有异常就认证成功跳转页面，有异常的话，走shiro底层quickStart的异常登录
        try {
            subject.login(token);
            SavedRequest savedRequest= WebUtils.getSavedRequest(req);
            if(null!=savedRequest) {
                String requestUrl = savedRequest.getRequestUrl();
                System.out.println("注意登录之前的路径是" + requestUrl);
                return "redirect:" + requestUrl;
            }
        } catch (IncorrectCredentialsException e) {
            System.out.println(e);
            attr.addAttribute("msg","密码错误！");
            return "redirect:/login";
        } catch (UnknownAccountException e) {
            System.out.println(e);
            attr.addAttribute("msg", "用户名不存在！");
            return "redirect:/login";
        }//密码不存在，还是返回Login页
        return "redirect:/";
    }
    @GetMapping("/toAbout")
    public String toAbout(Model model){
        return "about";
    }
    @RequiresPermissions("post:view")
    @GetMapping("/toBack")
    public String toBack(Model model){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String username = user.getName();
        Pageable pageable = PageRequest.of(0,8);
        List<Post> posts = postRepository.findByAuthor(username,pageable);
        int view=0,star=0,comment=0,collection=0;
        for (Post post: posts) {
            view += post.getView();
            star += post.getStar();
            comment += post.getComments().size();
            collection += post.getCollection();
        }
        List<Comment> comments = commentRepository.findByName(username);
        model.addAttribute("comments",comments);
        model.addAttribute("posts",posts);
        model.addAttribute("username",username);
        model.addAttribute("view",view);
        model.addAttribute("star",star);
        model.addAttribute("comment",comment);
        model.addAttribute("collection",collection);
        return "personal-admin";
    }
    @GetMapping("/addComment")
    public String addComment(String comment,Integer postId,RedirectAttributes attr){
        String username;
        User user = new User();
        try {
            user = (User) SecurityUtils.getSubject().getPrincipal();
        }catch (Exception e){
            System.out.println(e);
        }
        if(user!=null) {
            username = user.getName();
        }
        else {
            return "redirect:/login";
        }
        Comment com = new Comment();
        com.setName(username);
        com.setDate(new Date());
        if(!comment.equals("")){com.setText(comment);}
        com.setPost(postRepository.getById(postId));
        commentRepository.save(com);
        attr.addAttribute("id",postId);
        return "redirect:/toPost/{id}/show";
    }
    @GetMapping("/getPostByPage/{page}/show")
    public  String getPostByPage(@PathVariable("page") int page,Model model){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user!=null) {
            String username = user.getName();
            model.addAttribute("username", username);
        }
        Pageable pageable = PageRequest.of(page-1,3);
        Page<Post> posts = postRepository.findAll(pageable);
        List<Post> postsViewTop = postRepository.findTop6ByOrderByViewDesc();
        List<Post> postsLeast = postRepository.findTop5ByOrderByIdDesc();
        List<Comment> commentsLeast = commentRepository.findTop5ByOrderByIdDesc();
        long totalCount = postRepository.count();
        long maxPage = totalCount/3+1;
        model.addAttribute("posts",posts);
        model.addAttribute("postsViewTop",postsViewTop);
        model.addAttribute("postsLeast",postsLeast);
        model.addAttribute("commentsLeast",commentsLeast);
        model.addAttribute("currentPage",page);
        model.addAttribute("maxPage",maxPage);
        model.addAttribute("totalCount",totalCount);
        return "index";
    }
    @GetMapping("/toSignUp")
    public String toSignUp(Model model,String msg){
        model.addAttribute("msg",msg);
        return "sign-in";
    }
    @RequestMapping("/signUp")
    public String signUp(String name,String password,RedirectAttributes attr){
        List<Role> roles = List.of(roleRepository.getById(2));
        User user = userRepository.findByName(name);
        if(user!=null){
            attr.addAttribute("msg","用户名已存在！");
            return "redirect:/toSignUp";
        }else {
            Hash hashPwd = new SimpleHash("md5",password,null,1);
            User user1 = new User();
            user1.setRoles(roles);
            user1.setName(name);
            user1.setPassword(hashPwd.toString());
            userRepository.save(user1);
        return "redirect:/login";
        }
    }
    @RequestMapping("/categories")
    public String categories(Model model){
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories",categories);
        return "category";
    }
    @RequestMapping("/tags")
    public String tags(Model model){
        List<Tag> tags = tagRepository.findAll();
        model.addAttribute("tags",tags);
        return "tag";
    }
    @RequestMapping("/like/{pid}/")
    public String like(@PathVariable("pid") Integer pid){
        Post post = postRepository.getById(pid);
        post.setStar(post.getStar()+1);
        postRepository.save(post);
        return "redirect:/toPost/"+pid+"/show";
    }
}
