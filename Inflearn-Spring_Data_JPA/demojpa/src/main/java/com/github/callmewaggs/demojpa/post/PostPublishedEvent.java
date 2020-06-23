package com.github.callmewaggs.demojpa.post;

import org.springframework.context.ApplicationEvent;

public class PostPublishedEvent extends ApplicationEvent {

  private final Post post;

  // 이벤트를 발생시키는 곳이 Post 라 가정하므로
  public PostPublishedEvent(Object source) {
    super(source);
    this.post = (Post) source;
  }

  // 이벤트를 받는 리스너쪽에서 어떤 Post 에 대한 이벤트였는지 참조할 수 있도록
  public Post getPost() {
    return post;
  }
}
