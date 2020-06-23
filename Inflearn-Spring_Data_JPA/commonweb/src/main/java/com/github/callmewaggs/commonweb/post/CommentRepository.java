package com.github.callmewaggs.commonweb.post;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  <T> List<T> findByPost_Id(Long id, Class<T> type);
}
