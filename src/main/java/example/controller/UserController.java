package example.controller;

import example.dto.PostDto;
import example.dto.UserDto;
import example.model.Post;
import example.model.User;
import example.repository.PostRepository;
import example.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserController(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @PostMapping
    public UserDto createUser(@RequestBody User user) {
        if (user.getPosts() == null) {
            user.setPosts(new ArrayList<>()); // prevent NullPointerException
        }
        return new UserDto(userRepository.save(user));
    }

    @PostMapping("/{userId}/posts")
    public UserDto addPostToUser(@PathVariable String userId, @RequestBody Post post) {
        Post savedPost = postRepository.save(post);
        User user = userRepository.findById(userId).orElseThrow();

        if (user.getPosts() == null) {
            user.setPosts(new ArrayList<>());
        }

        user.getPosts().add(savedPost);
        return new UserDto(userRepository.save(user));
    }

    @GetMapping("/{id}")
    public UserDto getUserWithPosts(@PathVariable String id) {
        return new UserDto(userRepository.findById(id).orElseThrow());
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/posts")
    public List<PostDto> getUserPosts(@PathVariable String id) {
        return userRepository.findById(id)
                .orElseThrow()
                .getPosts() // lazy-loaded
                .stream()
                .map(PostDto::new)
                .collect(Collectors.toList());
    }
}
