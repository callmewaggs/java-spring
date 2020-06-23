package com.github.callmewaggs.commonweb.post;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comment {

  @Id @GeneratedValue private Long id;

  private String comment;

  @Enumerated(value = EnumType.STRING)
  private CommentStatus commentStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  private Post post;

  private int up;

  private int down;

  private boolean best;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }
}
