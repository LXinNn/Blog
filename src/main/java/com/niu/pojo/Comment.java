package com.niu.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.niu.repository.UserRepository;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Date date;
    private String text;
    @Column(insertable = false,columnDefinition = "int default 0")
    private Integer star;
    @JsonIgnoreProperties(value = {"comments"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "PostComment",joinColumns = @JoinColumn(name = "cid"),inverseJoinColumns = @JoinColumn(name = "postId"))
    private Post post;

    @Override
    public String toString() {
        return text;
    }
}
