package com.niu.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private String text;
    @JsonIgnoreProperties(value = {"tags"})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TagPost",joinColumns = @JoinColumn(name = "tagId"),inverseJoinColumns = @JoinColumn(name = "postId"))
    private Set<Post> posts;
}
