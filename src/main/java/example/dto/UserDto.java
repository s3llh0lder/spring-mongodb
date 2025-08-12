package example.dto;

import example.model.User;

public class UserDto {
    private String id;
    private String name;
    private String email;

    // Constructor
    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
