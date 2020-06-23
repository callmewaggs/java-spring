package com.github.callmewaggs.demospringdata;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comment {

  @Id @GeneratedValue private Long id;

  private String comment;

  @ManyToOne private Post post;

  private int likeCount = 0;

  private LocalDateTime created;

  public Comment(String comment) {
    this.comment = comment;
  }

  public void setLikeCount(int likeCount) {
    this.likeCount = likeCount;
  }

  public Long getId() {
    return id;
  }

  public String getComment() {
    return comment;
  }

  public Post getPost() {
    return post;
  }

  public int getLikeCount() {
    return likeCount;
  }

  public LocalDateTime getCreated() {
    return created;
  }
}
