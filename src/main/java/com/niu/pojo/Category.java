package com.niu.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private String text;
    @JsonIgnoreProperties(value = {"categories"})
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "CategoryPost",joinColumns = @JoinColumn(name = "cateId"),inverseJoinColumns = @JoinColumn(name = "postId"))
    private Set<Post> posts;

}
