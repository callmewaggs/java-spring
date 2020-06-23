package com.github.callmewaggs.demospringdata;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Post {
  @Id @GeneratedValue
  private Long id;

  private String title;

  @OneToMany(mappedBy = "post")
  private Set<Comment> comments = new HashSet<>();

  public Post(String title) {
    this.title = title;
  }

  public void addComment(Comment comment) {
    comments.add(comment);
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public Set<Comment> getComments() {
    return comments;
  }
}
