package com.github.callmewaggs.demojpa.post;

import java.util.List;

public interface PostCustomRepository<T> {
  List<Post> findMyPost();

  void delete(T entity);
}
