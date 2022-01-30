package com.anda.user.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserInfoResponse
{
    private String token;

    private String type = "Bearer";

    private String username;

    private String email;

    private List<String> roles;

    public UserInfoResponse(String token, String username, String email, List<String> roles)
    {
        this.token = token;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public UserInfoResponse(String token, String type, String username, String email, List<String> roles)
    {
        this.token = token;
        this.type = type;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
