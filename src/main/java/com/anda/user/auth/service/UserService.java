package com.anda.user.auth.service;

import com.anda.user.auth.model.User;

import java.util.List;

public interface UserService
{
    User save(User user);

    List<User> saveAll (List<User> users);
}
