package com.openclassrooms.starterjwt.controllers.teacher;

import com.openclassrooms.starterjwt.dto.TeacherDto;
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
public class TeacherControllerTestEnd2End {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    private final HttpHeaders httpHeaders = new HttpHeaders();

    @BeforeEach
    public void setUp() {
        String url = "http://localhost:" + port + "/api/auth/login";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("johndoe2@gmail.com");
        loginRequest.setPassword("a1b2c3");
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest);
        ResponseEntity<JwtResponse> playerResponseEntity = this.restTemplate.exchange(url, HttpMethod.POST, request, JwtResponse.class);
        httpHeaders.set("Authorization", "Bearer " + playerResponseEntity.getBody().getToken());
    }

    @Test
    public void shouldListTeachers() {
        // When
        String url = "http://localhost:" + port + "/api/teacher";
        HttpEntity<?> request = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<TeacherDto[]> teacherResponse = this.restTemplate.exchange(url, HttpMethod.GET, request, TeacherDto[].class);

        // Then
        Assertions.assertThat(teacherResponse.getBody().length).isEqualTo(2);
    }

    @Test
    public void shouldRetrieveTeacher() {
        // Given
        long teacherId = 1L;

        // When
        String url = "http://localhost:" + port + "/api/teacher/" + teacherId;
        HttpEntity<?> request = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<TeacherDto> teacherResponse = this.restTemplate.exchange(url, HttpMethod.GET, request, TeacherDto.class);

        // Then
        Assertions.assertThat(teacherResponse.getBody().getFirstName()).isEqualTo("JohnTeacher");
        Assertions.assertThat(teacherResponse.getBody().getLastName()).isEqualTo("DoeTeacher");
    }


    @Test
    public void shouldFailRetrieveTeacher_WhenTeacherNotFound() {
        // Given
       long teacherId = 999L;

        // When
        String url = "http://localhost:" + port + "/api/teacher/" + teacherId;
        HttpEntity<TeacherDto> request = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<TeacherDto> teacherResponse = this.restTemplate.exchange(url, HttpMethod.GET, request, TeacherDto.class);

        // Then
        Assertions.assertThat(teacherResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }



}