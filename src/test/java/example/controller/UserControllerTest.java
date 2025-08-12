package example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.model.Post;
import example.model.User;
import example.repository.PostRepository;
import example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private Post post;

    @BeforeEach
    void setup() {
        post = new Post();
        post.setId("postId1");
        post.setTitle("Sample Title");
        post.setContent("Sample Content");

        user = new User();
        user.setId("userId1");
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPosts(new ArrayList<>(List.of(post)));
    }

    @Test
    void testCreateUser() throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    void testAddPostToUser() throws Exception {
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(userRepository.findById("userId1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/userId1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    void testGetUserWithPosts() throws Exception {
        when(userRepository.findById("userId1")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/userId1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("john@example.com")));
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is("userId1")));
    }

    @Test
    void testGetUserPosts() throws Exception {
        when(userRepository.findById("userId1")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/userId1/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Sample Title")));
    }
}