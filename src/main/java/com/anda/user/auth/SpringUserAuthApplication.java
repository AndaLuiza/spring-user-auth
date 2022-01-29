package com.anda.user.auth;

import com.anda.user.auth.model.Role;
import com.anda.user.auth.model.RoleEnum;
import com.anda.user.auth.model.User;
import com.anda.user.auth.service.RoleService;
import com.anda.user.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

@SpringBootApplication
public class SpringUserAuthApplication implements CommandLineRunner
{
    @Autowired
    private RoleService roleServiceImpl;

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args)
    {
        SpringApplication.run(SpringUserAuthApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        var roles = new ArrayList<Role>();
        Arrays.asList(RoleEnum.values()).forEach(roleEnum -> roles.add(new Role(roleEnum)));
        roleServiceImpl.saveAll(roles);

        roleServiceImpl.findAll().forEach(System.out::println);

        Role adminRole = roleServiceImpl.findByName(RoleEnum.ROLE_ADMIN).get();
        Role userRole = roleServiceImpl.findByName(RoleEnum.ROLE_USER).get();

        User newUser = new User(
                "user1",
                passwordEncoder.encode("test1"),
                "test1@email.com",
                Set.of(adminRole, userRole));

        userServiceImpl.save(newUser);
    }
}
