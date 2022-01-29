package com.anda.user.auth.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class UserInfoResponse
{
    private final String token;

    private String type = "Bearer";

    private final String username;

    private final String email;

    private final List<String> roles;
}
