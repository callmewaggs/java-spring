package com.github.callmewaggs.demojpa.post;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
public class Post extends AbstractAggregateRoot<Post> {

  @Id
  @GeneratedValue
  private Long id;

  private String title;

  @Lob // 본문은 255자가 넘을 수 있기 때문에 이렇게 설정해 줌.
  private String content;

  @Temporal(TemporalType.TIMESTAMP)
  private Date created;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  // 이벤트 날리는 도메인 메서드
  public Post publish() {
    this.registerEvent(new PostPublishedEvent(this));
    return this;
  }
}
