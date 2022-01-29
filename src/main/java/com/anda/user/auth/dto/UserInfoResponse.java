package com.anda.user.auth.dto;

import com.anda.user.auth.model.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfoResponse
{
    private String username;

    private String email;

    private List<RoleEnum> roles;
}
