package com.github.callmewaggs.demospringmvc.user;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  public void hello() throws Exception {
    mockMvc
        .perform(get("/hello"))
        .andExpect(status().isOk())
        .andExpect(content().string("hello"))
        .andDo(print());
  }

  @Test
  public void createUser_JSON() throws Exception {
    // Arrange
    String userJson = "{\"username\":\"waggs\", \"password\":\"pass\"}";

    // Act
    ResultActions actual =
        mockMvc.perform(
            post("/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userJson));

    // Assert
    actual
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username", is(equalTo("waggs"))))
        .andExpect(jsonPath("$.password", is(equalTo("pass"))));
  }

  @Test
  public void createUser_request_with_JSON_response_with_XML() throws Exception {
    // Arrange
    String userJson = "{\"username\":\"waggs\", \"password\":\"pass\"}";

    // Act
    ResultActions actual =
        mockMvc.perform(
            post("/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_XML)
                .content(userJson));

    // Assert
    actual
        .andExpect(status().isOk())
        .andExpect(xpath("/User/username").string("waggs"))
        .andExpect(xpath("/User/password").string("pass"));
  }
}
