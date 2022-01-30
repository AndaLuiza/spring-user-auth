package com.anda.user.auth;

import com.anda.user.auth.dto.AuthRequest;
import com.anda.user.auth.dto.UserInfoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class SpringUserAuthIntegrationTests
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenBadCredentials_whenAttemptToAuthenticate_shouldReturn401Unauthorized() throws Exception
    {
        String username = "user1";
        String password = "test2";

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(username);
        authRequest.setPassword(password);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenAuthenticated_whenAccessUserDataWithValidToken_shouldReturn200Ok() throws Exception
    {
        String username = "user1";
        String password = "test1";
        String token = getTokenAfterAuthentication(username, password);

        assertNotNull(token);
        mockMvc.perform(MockMvcRequestBuilders.
                        get("/api/auth/currentUser")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void givenAuthenticated_whenAccessUserDataWithBadToken_shouldReturn401Unauthorized() throws Exception
    {
        String username = "admin";
        String password = "admin";
        String token = getTokenAfterAuthentication(username, password);

        assertNotNull(token);

        String badToken = token + "kj";

        mockMvc.perform(MockMvcRequestBuilders.
                        get("/api/auth/currentUser")
                        .header("Authorization", "Bearer " + badToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenNotAuthenticated_whenAttemptToAccessUserDataWithNoToken_shouldReturn401Unauthorized() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.
                        get("/api/auth/currentUser")
                        .header("Authorization", "Bearer "))
                .andExpect(status().isUnauthorized());
    }

    private String getTokenAfterAuthentication(String username, String password) throws Exception
    {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(username);
        authRequest.setPassword(password);

        MvcResult authResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(status().isOk()).andReturn();

        String authResponse = authResult.getResponse().getContentAsString();

        UserInfoResponse objectMapper = new ObjectMapper().readValue(authResponse, UserInfoResponse.class);
        return objectMapper.getToken();
    }

}
