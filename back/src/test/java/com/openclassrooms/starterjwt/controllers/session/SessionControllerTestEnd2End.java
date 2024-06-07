package com.openclassrooms.starterjwt.controllers.session;

import com.openclassrooms.starterjwt.dto.SessionDto;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SessionControllerTestEnd2End {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    private final HttpHeaders httpHeaders = new HttpHeaders();

    @BeforeEach
    public void setUp(){
        String url = "http://localhost:" + port +"/api/auth/login";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("johndoe2@gmail.com");
        loginRequest.setPassword("a1b2c3");
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest);
        ResponseEntity<JwtResponse> playerResponseEntity = this.restTemplate.exchange(url, HttpMethod.POST, request, JwtResponse.class);
        httpHeaders.set("Authorization", "Bearer " + playerResponseEntity.getBody().getToken());
    }
    @Test
    public void shouldCreateSession(){
        // Given
        SessionDto sessionToCreate = new SessionDto(
                null,
                "Une session",
                Date.from(Instant.now()),
                1L,
                "Une description",
                Arrays.asList(1L, 2L),
                LocalDateTime.now(),
                null
        );

        // When
        String url = "http://localhost:" + port +"/api/session";
        HttpEntity<SessionDto> request = new HttpEntity<>(sessionToCreate, httpHeaders);
        ResponseEntity<SessionDto> sessionResponse = this.restTemplate.exchange(url, HttpMethod.POST, request, SessionDto.class);

        // Then
        Assertions.assertThat(sessionResponse.getBody().getId()).isEqualTo(3L);
        Assertions.assertThat(sessionResponse.getBody().getName()).isEqualTo("Une session");
        Assertions.assertThat(sessionResponse.getBody().getDescription()).isEqualTo("Une description");

    }


    @Test
    public void shouldFailToCreateSession_WhenSessionDtoIsNotValid(){
        // Given
        SessionDto sessionToCreate = new SessionDto(
                null,
                null,
                Date.from(Instant.now()),
                1L,
                "Une description",
                Arrays.asList(1L, 2L),
                LocalDateTime.now(),
                null
        );

        // When
        String url = "http://localhost:" + port +"/api/session";
        HttpEntity<SessionDto> request = new HttpEntity<>(sessionToCreate, httpHeaders);
        ResponseEntity<SessionDto> sessionResponse = this.restTemplate.exchange(url, HttpMethod.POST, request, SessionDto.class);

        // Then
        Assertions.assertThat(sessionResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }


    @Test
    public void shouldParticipateToSession(){
        // Given
        Long sessionId = 1L;
        Long userId = 3L;

        // When
        String url = "http://localhost:" + port +"/api/session/" + sessionId + "/participate/" + userId;
        HttpEntity<SessionDto> request = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<?> sessionResponse = this.restTemplate.exchange(url, HttpMethod.POST, request, Object.class);

        // Then
        Assertions.assertThat(sessionResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    }
}
