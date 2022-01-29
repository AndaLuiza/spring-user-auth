package com.anda.user.auth.controller;

import com.anda.user.auth.security.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/auth")
@RestController
public class AuthController
{
    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping("login")
    public void authenticate()
    {
        // TODO : authenticate, get user principal
    }

    @PostMapping("logout")
    public void logout()
    {
        // TODO
    }

    @GetMapping(value = "user/{username}")
    public void loadUserDetails(@PathVariable String username)
    {
        // TODO : load user details
    }
}
