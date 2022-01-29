package com.anda.user.auth.controller;

import com.anda.user.auth.dto.AuthRequest;
import com.anda.user.auth.dto.UserInfoResponse;
import com.anda.user.auth.model.RoleEnum;
import com.anda.user.auth.security.service.MyUserDetailsService;
import com.anda.user.auth.security.service.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("api/auth")
@RestController
public class AuthController
{
    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping("login")
    public void authenticate(@RequestBody AuthRequest authRequest)
    {
        // TODO : authenticate, get user principal
    }

    @PostMapping("logout")
    public void logout()
    {
        // TODO
    }

    @GetMapping(value = "user/{username}")
    public UserInfoResponse loadUserDetails(@PathVariable String username)
    {
        // TODO : load user details
        UserPrincipal principal = (UserPrincipal) userDetailsService.loadUserByUsername(username);

        List<RoleEnum> roles =  principal.getAuthorities()
                .stream()
                .map(grantedAuthority -> RoleEnum.valueOf(grantedAuthority.getAuthority()))
                .collect(Collectors.toList());

        return new UserInfoResponse(principal.getUsername(), principal.getEmail(), roles);
    }
}
