package com.anda.user.auth.service;

import com.anda.user.auth.model.User;
import com.anda.user.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;

    @Override
    public User save(User user)
    {
        return userRepository.save(user);
    }
}
