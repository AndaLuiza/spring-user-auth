package com.anda.user.auth.service;

import com.anda.user.auth.model.User;
import com.anda.user.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User save(User user)
    {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public List<User> saveAll(List<User> users)
    {
        return userRepository.saveAll(users);
    }
}
