package com.openclassrooms.starterjwt.controllers.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AuthenticationManager authenticationManager;
    @MockBean
    Authentication authentication;
    @MockBean
    JwtUtils jwtUtils;
    @MockBean
    PasswordEncoder passwordEncoder;
    @MockBean
    UserRepository userRepository;

    ObjectMapper objectMapper = new ObjectMapper()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new JavaTimeModule());

    ObjectWriter objectWriter = objectMapper.writer();
    private List<Session> sessions;

    private List<SessionDto> sessionsDto;

    @Test
    public void shouldNotLoginAdmin_WhenUserDoesntExist() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("jeanfrancois@gmail.com");
        loginRequest.setPassword("a1b2c3");

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())))
                .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication))
                .thenReturn("token");
        when(authentication.getPrincipal())
                .thenReturn(new UserDetailsImpl(1L, "jeanfrancois@gmail.com", "Francois", "Jean", false, "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW"));

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        String content = objectWriter.writeValueAsString(loginRequest);

        // When / Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.admin", CoreMatchers.is(false)))
                .andExpect(jsonPath("$.token", CoreMatchers.is("token")));

    }

    @Test
    public void shouldLoginAdmin() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("jeanfrancois@gmail.com");
        loginRequest.setPassword("a1b2c3");

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())))
                .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication))
                .thenReturn("token");
        when(authentication.getPrincipal())
                .thenReturn(new UserDetailsImpl(1L, "jeanfrancois@gmail.com", "Francois", "Jean", true, "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW"));

        User user = new User(1L,"jeanfrancois@gmail.com", "Jean", "Francois",  "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW", true, LocalDateTime.now(), null);
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));

        String content = objectWriter.writeValueAsString(loginRequest);

        // When / Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.admin", CoreMatchers.is(true)))
                .andExpect(jsonPath("$.token", CoreMatchers.is("token")));

    }


    @Test
    public void shouldRegister() throws Exception {
        // Given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("jeanfrancois@gmail.com");
        signupRequest.setPassword("a1b2c3");
        signupRequest.setFirstName("Jean");
        signupRequest.setLastName("François");

        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encoded");

        String content = objectWriter.writeValueAsString(signupRequest);

        // When / Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.message", CoreMatchers.is("User registered successfully!")));


    }

    @Test
    public void shouldFailToRegister400_WhenUserAlreadyExist() throws Exception {
        // Given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("jeanfrancois@gmail.com");
        signupRequest.setPassword("a1b2c3");
        signupRequest.setFirstName("Jean");
        signupRequest.setLastName("François");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        String content = objectWriter.writeValueAsString(signupRequest);

        // When / Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message", CoreMatchers.is("Error: Email is already taken!")));


    }


}
