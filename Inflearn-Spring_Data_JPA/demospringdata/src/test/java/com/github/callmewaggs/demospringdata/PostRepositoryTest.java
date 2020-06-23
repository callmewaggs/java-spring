package com.github.callmewaggs.demospringdata;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest // Slicing test 를 위해 DataJpa 만을 테스트하겠다는 것. Spring boot 의 기능. 다른 빈 들은 등록이 안되고 Repository 관련 빈 만 등록됨.
public class PostRepositoryTest {

  // H2 Database 를 추가했으므로, 실제 운영에서 사용할 postgres DB 에는 영향을 주지 않기 위해 메모리 DB인 H2 만을 사용하게 됨.
  @Autowired
  PostRepository postRepository;

  @Test
  @Rollback(false)
  public void crudRepositoryTest() {
    // Given - 이런 조건에서
    Post post = new Post("Hello Spring Boot Common");
    assertThat(post.getId()).isNull();

    // When - 이렇게 했을때
    Post newPost = postRepository.save(post);

    // Then - 이렇게 되기를 기대한다
    assertThat(newPost.getId()).isNotNull();

    // When
    List<Post> posts = postRepository.findAll();

    // Then
    assertThat(posts.size()).isEqualTo(1);
    assertThat(posts).contains(post);

    // When
    Page<Post> page = postRepository.findAll(PageRequest.of(0, 10));

    // Then
    assertThat(page.getTotalElements()).isEqualTo(1);
    assertThat(page.getNumber()).isEqualTo(0);
    assertThat(page.getSize()).isEqualTo(10);
    assertThat(page.getNumberOfElements()).isEqualTo(1);

    // When
    page = postRepository.findByTitleContains("Spring", PageRequest.of(0, 10));

    // Then
    assertThat(page.getTotalElements()).isEqualTo(1);
    assertThat(page.getNumber()).isEqualTo(0);
    assertThat(page.getSize()).isEqualTo(10);
    assertThat(page.getNumberOfElements()).isEqualTo(1);

    // When
    long count = postRepository.countByTitleContains("Spring");

    // Then
    assertThat(count).isEqualTo(1);
  }
}
