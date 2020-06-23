package com.github.callmewaggs.commonweb.post;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PostRepositoryTest {

  @Autowired private PostRepository postRepository;

  @Test
  public void findByTitleStartsWith_test() {
    Post post = new Post();
    post.setTitle("spring data jpa");
    postRepository.save(post); // persist

    List<Post> all = postRepository.findByTitleStartsWith("spring");
    assertEquals(1, all.size());
  }

  @Test
  public void findByTitle_test() {
    Post post = new Post();
    post.setTitle("Spring");
    postRepository.save(post);

    List<Post> all = postRepository.findByTitle("Spring", JpaSort.unsafe("LENGTH(title)"));
    assertEquals(1, all.size());
  }

  @Test
  public void updateTitle() {
    // Arrange
    Post spring = new Post();
    spring.setTitle("spring");
    postRepository.save(spring);

    // Act
    spring.setTitle("Hibernate");

    // Actual
    List<Post> all = postRepository.findAll();
    assertEquals("Hibernate", all.get(0).getTitle());
  }
}
