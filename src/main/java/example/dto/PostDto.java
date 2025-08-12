package example.dto;

import example.model.Post;

public class PostDto {
    private String id;
    private String title;
    private String content;

    public PostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
}
