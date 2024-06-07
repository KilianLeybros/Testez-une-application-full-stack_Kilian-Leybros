package com.openclassrooms.starterjwt.controllers.user;


import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;
    @MockBean
    UserMapper userMapper;


    @Test
    @WithMockUser
    public void shouldRetrieveUser() throws Exception {
        // Given
        long userId = 1L;
        User user =   new User(1L,"jeanfrancois@gmail.com", "Jean", "Francois",  "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW", true, LocalDateTime.now(), null);
        UserDto userDto = new UserDto(1L,"jeanfrancois@gmail.com", "Jean", "Francois",   true,"$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW", LocalDateTime.now(), null);
        when(userService.findById(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // When / Then
        mockMvc.perform(get("/api/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", CoreMatchers.is("Jean")))
                .andExpect(jsonPath("$.firstName", CoreMatchers.is("Francois")))
                .andExpect(jsonPath("$.email", CoreMatchers.is("jeanfrancois@gmail.com")));

    }

    @Test
    public void shouldFailToRetrieveUser_WhenUnauthorized() throws Exception {
        // Given
        long userId = 1L;
        User user =   new User(1L,"jeanfrancois@gmail.com", "Jean", "Francois",  "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW", true, LocalDateTime.now(), null);
        UserDto userDto = new UserDto(1L,"jeanfrancois@gmail.com", "Jean", "Francois",   true,"$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW", LocalDateTime.now(), null);
        when(userService.findById(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // When / Then
        mockMvc.perform(get("/api/user/" + userId))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser
    public void shouldFailToRetrieveUser404_WhenUserNotFound() throws Exception {
        // Given
        long userId = 999L;
        when(userService.findById(userId)).thenReturn(null);

        // When / Then
        mockMvc.perform(get("/api/user/" + userId))
                .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser
    public void shouldFailToRetrieveUser400_WhenUserIdIsNotANumber() throws Exception {
        // Given
        String userId = "AAA";

        // When / Then
        mockMvc.perform(get("/api/user/" + userId))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "jeanfrancois@gmail.com", password = "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW")
    public void shouldDeleteUser() throws Exception {
        // Given
        long userId = 1L;
        User user = new User(1L,"jeanfrancois@gmail.com", "Jean", "Francois",  "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW", true, LocalDateTime.now(), null);
        when(userService.findById(userId)).thenReturn(user);

        // When / Then
        mockMvc.perform(delete("/api/user/" + userId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.com", password = "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW")
    public void shouldFailedToDeleteUser_WhenUserUnauthorized() throws Exception {
        // Given
        long userId = 1L;
        User user = new User(1L,"jeanfrancois@gmail.com", "Jean", "Francois",  "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW", true, LocalDateTime.now(), null);
        when(userService.findById(userId)).thenReturn(user);

        // When / Then
        mockMvc.perform(delete("/api/user/" + userId))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser
    public void shouldFailToDeleteUser404_WhenUserNotFound() throws Exception {
        // Given
        long userId = 999L;
        when(userService.findById(userId)).thenReturn(null);

        // When / Then
        mockMvc.perform(delete("/api/user/" + userId))
                .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser
    public void shouldFailToDeleteUser400_WhenUserIdIsNotANumber() throws Exception {
        // Given
        String userId = "AAA";

        // When / Then
        mockMvc.perform(delete("/api/user/" + userId))
                .andExpect(status().isBadRequest());

    }

}
