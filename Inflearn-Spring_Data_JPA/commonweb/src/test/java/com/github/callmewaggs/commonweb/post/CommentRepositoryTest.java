package com.github.callmewaggs.commonweb.post;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CommentRepositoryTest {

  @Autowired private CommentRepository commentRepository;

  @Autowired private PostRepository postRepository;

  @Test
  public void getComment() {
    commentRepository.findByPost_Id(1L, CommentOnly.class);
  }
}
