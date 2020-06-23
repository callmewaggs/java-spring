package com.github.callmewaggs.demospringdata;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CommentRepositoryTest {

  @Autowired CommentRepository commentRepository;

  @Test
  public void findByCommentContainsIgnoreCase_test()
      throws ExecutionException, InterruptedException {
    // Arrange
    Comment comment = new Comment("spring data jpa");
    commentRepository.save(comment);

    // Act
    // 아래 이 call 자체는 non-blocking 이 될 수 있으나,
    Future<List<Comment>> actual = commentRepository.findByCommentContainsIgnoreCase("spring");

    // Assert
    System.out.println(actual.isDone()); // isDone() 호출시 결과가 나왔는지 확인 하는 것
    // get() 호출과 동시에 blocking 이 됨.
    List<Comment> comments = actual.get(); // get() 호출시 결과가 나올 때 까지 기다림
    comments.forEach(System.out::println);
  }
}
