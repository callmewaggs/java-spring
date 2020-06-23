package com.github.callmewaggs.demojpa.post;

import com.github.callmewaggs.demojpa.MyRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PostRepository extends MyRepository<Post, Long>,
    QuerydslPredicateExecutor<Post> {}
