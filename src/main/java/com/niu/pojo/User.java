package com.niu.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String password;
    @JsonIgnoreProperties(value = {"users"})
    //每个用户可以有多种角色，FetchType.EAGER：立即从数据库加载
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "UserRole",joinColumns = @JoinColumn(name = "uid"),inverseJoinColumns = @JoinColumn(name = "rid"))
    private List<Role> roles;
}
