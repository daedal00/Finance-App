package com.daedal00.app.api.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String accessToken;
    private String itemId;

    public UserDTO() {}

    public UserDTO(String id, String username, String email, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
