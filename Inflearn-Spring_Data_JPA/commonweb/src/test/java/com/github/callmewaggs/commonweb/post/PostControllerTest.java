package com.github.callmewaggs.commonweb.post;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

  @Autowired PostRepository postRepository;

  @Autowired MockMvc mockMvc;

  @Test
  public void getPost_test() throws Exception {
    // Arrange
    Post post = new Post();
    post.setTitle("jpa");
    postRepository.save(post);

    // Act
    ResultActions actual = mockMvc.perform(get("/posts/" + post.getId())).andDo(print());

    // Assert
    actual.andExpect(status().isOk());
    actual.andExpect(content().string("jpa"));
  }

  @Test
  public void getPosts_test() throws Exception {
    // Arrange
    createPosts();

    // Act
    ResultActions actual =
        mockMvc
            .perform(
                get("/posts/")
                    .param("page", "3")
                    .param("size", "10")
                    .param("sort", "created,desc")
                    .param("sort", "title"))
            .andDo(print());

    // Assert
    actual.andExpect(status().isOk());
    actual.andExpect(jsonPath("$.content[0].title", is("jpa")));
  }

  private void createPosts() {
    int postsCount = 100;
    while (postsCount > 0) {
      Post post = new Post();
      post.setTitle("jpa");
      postRepository.save(post);
      postsCount--;
    }
  }
}
