package com.github.callmewaggs.demospringdata;

import java.util.List;
import java.util.concurrent.Future;
import org.springframework.scheduling.annotation.Async;

public interface CommentRepository extends MyRepository<Comment, Long> {

  @Async
  Future<List<Comment>> findByCommentContainsIgnoreCase(String keyword);
}
