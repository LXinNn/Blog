package com.niu.controller;

import com.niu.mapper.PostMapper;
import com.niu.pojo.Category;
import com.niu.pojo.Post;
import com.niu.pojo.Tag;
import com.niu.pojo.User;
import com.niu.repository.CategoryRepository;
import com.niu.repository.PostRepository;
import com.niu.repository.TagRepository;
import com.niu.repository.UserRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.*;

@Controller
public class PostActController {
    @Resource
    UserRepository userRepository;
    @Resource
    PostRepository postRepository;
    @Resource
    CategoryRepository categoryRepository;
    @Resource
    TagRepository tagRepository;
    //删除文章
    @RequiresPermissions("post:delete")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id){
        postRepository.deleteById(id);
        return "redirect:/toBack";
    }
    //到修改界面
    @RequiresPermissions("post:update")
    @GetMapping("/toUpdate/{id}/")
    public String toUpdateById(@PathVariable("id") Integer id,Model model){
        Post post = postRepository.getById(id);
        Set<Tag> posttag= post.getTags();
        boolean update = true;
        List<Tag> tags = tagRepository.findAll();
        for(Tag tag:posttag){
            if(tags.contains(tag)) {
                tags.remove(tag);
            }
        }
        model.addAttribute("update",update);
        model.addAttribute("post",post);
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories",categories);
        model.addAttribute("tags",tags);
        return "backstage/update";
    }
    //修改文章
    @RequiresPermissions("post:update")
    @GetMapping("/update/{id}/")
    public String update(String mdtext,String htmltext,String description,String category,String title,@PathVariable("id") Integer id,String ... tag){
        Post post = postRepository.getById(id);
        Set<Tag> tags = new HashSet<>();
        if(tag!=null) {
            for (String each : tag) {
                tags.add(tagRepository.findByText(each));
            }
            post.setTags(tags);
        }
        if(!category.equals("")){post.setCategory(categoryRepository.getByText(category));}
        if(!mdtext.equals("")){post.setMdtext(mdtext);}
        if(!htmltext.equals("")){post.setHtmltext(htmltext);}
        post.setDescription(description);
        if(title!=null){post.setTitle(title);}
        postRepository.save(post);
        return "redirect:/toBack";
    }
    @RequiresPermissions("post:add")
    @RequestMapping("/toPost")
    public String toPost(Model model){
        List<Category> categories = categoryRepository.findAll();
        List<Tag> tags = tagRepository.findAll();
        boolean update = false;
        model.addAttribute("categories",categories);
        model.addAttribute("tags",tags);
        model.addAttribute("update",update);
        return "backstage/update";
    }
    @RequiresPermissions("post:add")
    @RequestMapping("/post")
    public String post(Date date,String mdtext,String htmltext,String description,String category,String title,String ... tag){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Post post = new Post();
        Set<Tag> tags = new HashSet<>();
        if(tag!=null) {
            for (String each : tag) {
                tags.add(tagRepository.findByText(each));
            }
            post.setTags(tags);
        }
        if(!category.equals("")){post.setCategory(categoryRepository.getByText(category));}
        if(!mdtext.equals("")){post.setMdtext(mdtext);}
        if(!htmltext.equals("")){post.setHtmltext(htmltext);}
        post.setDate(date);
        post.setAuthor(user.getName());
        if(title!=null){post.setTitle(title);}
        post.setDescription(description);
        post.setStar(0);
        post.setView(0);
        post.setCollection(0);
        postRepository.save(post);
        return "redirect:/";
    }
}
