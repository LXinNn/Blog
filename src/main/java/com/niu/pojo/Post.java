package com.niu.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Proxy(lazy = false)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String author;
    private Date date;
    private String mdtext;
    private String htmltext;
    private String description;
    @Column(columnDefinition = "int default 0")
    private Integer star;
    @Column(columnDefinition = "int default 0")
    private Integer collection;
    @Column(columnDefinition = "int default 0")
    private Integer view;
    @JsonIgnoreProperties(value = {"posts"})
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "PostComment",joinColumns = @JoinColumn(name = "postId"),inverseJoinColumns = @JoinColumn(name = "cid"))
    private Set<Comment> comments;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "CategoryPost",joinColumns = @JoinColumn(name = "postId"),inverseJoinColumns = @JoinColumn(name = "cateId"))
    private Category category;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TagPost",joinColumns = @JoinColumn(name = "postId"),inverseJoinColumns = @JoinColumn(name = "tagId"))
    private Set<Tag> tags;
}
