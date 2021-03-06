package com.anda.user.auth.controller;

import com.anda.user.auth.dto.AuthRequest;
import com.anda.user.auth.dto.UserInfoResponse;
import com.anda.user.auth.model.Token;
import com.anda.user.auth.repository.TokenRepository;
import com.anda.user.auth.security.jwt.JwtTokenUtils;
import com.anda.user.auth.security.service.MyUserDetailsService;
import com.anda.user.auth.security.service.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private TokenRepository tokenRepository;

    @PostMapping("login")
    public ResponseEntity<UserInfoResponse> authenticate(@RequestBody AuthRequest authRequest)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtTokenUtils.generateJwtToken(authentication);

        // just a workaround for logout - invalidate jwt
        Token token = new Token();
        token.setToken(jwtToken);
        token.setValid(true);
        tokenRepository.save(token);

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new UserInfoResponse(jwtToken,
                principal.getUsername(),
                principal.getEmail(),
                roles));
    }

    @PostMapping("logout")
    public void logout()
    {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        SecurityContextHolder.clearContext();
    }

    @GetMapping("currentUser")
    public UserInfoResponse loadLoggedInUserDetails()
    {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new UserInfoResponse("", principal.getUsername(), principal.getEmail(), roles);
    }

    @GetMapping("main")
    @PreAuthorize("hasRole('USER') or hasRole('SUPER_USER')")
    public String userAccess() {
        return "Main";
    }

    @GetMapping("admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin";
    }
}
