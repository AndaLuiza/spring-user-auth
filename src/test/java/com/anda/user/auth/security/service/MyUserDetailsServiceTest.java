package com.anda.user.auth.security.service;

import com.anda.user.auth.model.Role;
import com.anda.user.auth.model.RoleEnum;
import com.anda.user.auth.model.User;
import com.anda.user.auth.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@RunWith(MockitoJUnitRunner.class)
public class MyUserDetailsServiceTest
{
    @Mock
    UserRepository userRepository;

    @InjectMocks
    MyUserDetailsService userDetailsService;

    @Before
    public void init()
    {
        openMocks(this);
    }

    @Test
    public void loadUserByUsernameTest()
    {
        String username = "admin";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(createNewUser(username)));

        UserPrincipal principal = (UserPrincipal) userDetailsService.loadUserByUsername(username);

        assertEquals("admin", principal.getUsername());
        assertEquals("encoded_password", principal.getPassword());

        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        assertEquals(List.of(RoleEnum.ROLE_ADMIN.toString()), roles);
    }

    private User createNewUser(String username)
    {
        return new User(
                username,
                "encoded_password",
                "admin@email.com",
                Set.of(new Role(RoleEnum.ROLE_ADMIN)));
    }
}
