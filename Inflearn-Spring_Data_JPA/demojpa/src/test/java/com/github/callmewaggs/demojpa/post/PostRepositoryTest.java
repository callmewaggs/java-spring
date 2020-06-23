package com.github.callmewaggs.demojpa.post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(PostRepositoryTestConfig.class) // 테스트를 실행할 때, 이 Config 를 참조하게끔
public class PostRepositoryTest {

  @Autowired PostRepository postRepository;

  @Test
  public void crud() {
    Post post1 = new Post();
    post1.setTitle("Spring Post");
    postRepository.save(post1.publish());

    Post post2 = new Post();
    post2.setTitle("Spring Hibernate");
    postRepository.save(post2.publish());

    Predicate predicate = QPost.post.title.containsIgnoreCase("Spring");
    List<Post> actual = (List<Post>) postRepository.findAll(predicate);
    assertEquals(2, actual.size());
  }
}
