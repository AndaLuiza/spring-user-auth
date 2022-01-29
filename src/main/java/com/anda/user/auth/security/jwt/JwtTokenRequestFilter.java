package com.anda.user.auth.security.jwt;

import com.anda.user.auth.model.Token;
import com.anda.user.auth.repository.TokenRepository;
import com.anda.user.auth.security.service.MyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Request filter to validate jwt token in the header of each request and to set the authentication data
 */
@Slf4j
public class JwtTokenRequestFilter extends OncePerRequestFilter
{
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        try
        {
            Optional<String> token = getTokenFromRequestHeader(request);
            token.ifPresentOrElse(
                    foundToken -> validateTokenAndSetAuthenticationData(request, foundToken),
                    () -> log.info("No access token provided in the request authorization header"));
        }
        catch (Exception e)
        {
            log.error("Cannot set user authentication: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    private void validateTokenAndSetAuthenticationData(HttpServletRequest request, String foundToken)
    {
        // in case of previous logout, this should not be reusable
        Optional<Token> dbToken = tokenRepository.findByToken(foundToken);
        var isValidAgainstDb = false;
        if (dbToken.isPresent())
        {
            isValidAgainstDb = dbToken.get().isValid();
        }

        if (isValidAgainstDb && jwtTokenUtils.isValidJwtToken(foundToken))
        {
            buildAuthenticationUser(request, jwtTokenUtils.getUsernameFromJwtToken(foundToken));

            // workaround to invalidate a jwt in case of logout (no session management)
            // this is just a sample; should be used another mechanism (outh2, cache redis)
            if (request.getRequestURI().contains("logout"))
            {
                // invalidate used token
                Token dbTokenToInvalidate = tokenRepository.findByToken(foundToken).get();
                dbTokenToInvalidate.setValid(false);
                tokenRepository.save(dbTokenToInvalidate);
            }
        }
    }

    private void buildAuthenticationUser(HttpServletRequest request, String username)
    {
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // add authentication into security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private static Optional<String> getTokenFromRequestHeader(HttpServletRequest request)
    {
        var authorizationHeader = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER))
        {
            return Optional.of(authorizationHeader.substring(7));
        }
        return Optional.empty();
    }

}
