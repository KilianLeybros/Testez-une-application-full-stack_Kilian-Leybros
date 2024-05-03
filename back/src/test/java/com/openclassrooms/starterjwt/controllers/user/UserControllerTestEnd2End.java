package com.openclassrooms.starterjwt.controllers.user;

import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTestEnd2End {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    private final HttpHeaders httpHeaders = new HttpHeaders();

    @BeforeEach
    public void setUp(){
        String url = "http://localhost:" + port +"/api/auth/login";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("johndoe4@gmail.com");
        loginRequest.setPassword("a1b2c3");
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest);
        ResponseEntity<JwtResponse> playerResponseEntity = this.restTemplate.exchange(url, HttpMethod.POST, request, JwtResponse.class);
        httpHeaders.set("Authorization", "Bearer " + playerResponseEntity.getBody().getToken());
    }
    @Test
    public void shouldDeleteUser(){
        // Given
        long userId = 4L;

        // When
        String url = "http://localhost:" + port +"/api/user/" + userId;
        HttpEntity<?> request = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<?> userResponse = this.restTemplate.exchange(url, HttpMethod.DELETE, request, Object.class);

        // Then
        Assertions.assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldFailToDeleteUser_WhenUserUnauthorized(){
        // Given
        long userId = 5L;

        // When
        String url = "http://localhost:" + port +"/api/user/" + userId;
        HttpEntity<?> request = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<?> userResponse = this.restTemplate.exchange(url, HttpMethod.DELETE, request, Object.class);

        // Then
        Assertions.assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

}
