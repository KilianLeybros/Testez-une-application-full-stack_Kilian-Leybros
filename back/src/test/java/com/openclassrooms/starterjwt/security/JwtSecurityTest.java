package com.openclassrooms.starterjwt.security;

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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JwtSecurityTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;
    @MockBean
    UserMapper userMapper;

    @Autowired
    JwtUtils jwtUtils;

    @MockBean
    Authentication authentication;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @Value("${oc.app.jwtSecret}")
    private String jwtSecret;

    @Value("${oc.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Test
    public void shouldBeAuthorized_WithToken() throws Exception {
        // Given
        long userId = 1L;
        User user =   new User(1L,"jeanfrancois@gmail.com", "Jean", "Francois",  "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW", true, LocalDateTime.now(), null);
        UserDto userDto = new UserDto(1L,"jeanfrancois@gmail.com", "Jean", "Francois",   true,"$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW", LocalDateTime.now(), null);
        when(userService.findById(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDetails userDetails = new UserDetailsImpl(1L,"johndoe@gmail.com", "Doe", "John", true, "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetailsService.loadUserByUsername(userDetails.getUsername())).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);
        // When / Then
        mockMvc.perform(get("/api/user/" + userId)
                        .header("Authorization", "Bearer "+token))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void shouldBeUnauthorized_WithUnknownUserInToken() throws Exception {
        // Given
        long userId = 1L;

        UserDetails userDetails = new UserDetailsImpl(1L,"johndoe@gmail.com", "Doe", "John", true, "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetailsService.loadUserByUsername(userDetails.getUsername())).thenReturn(null);
        String token = jwtUtils.generateJwtToken(authentication);
        // When / Then
        mockMvc.perform(get("/api/user/" + userId)
                        .header("Authorization", "Bearer "+token))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", CoreMatchers.is("Unauthorized")))
                .andExpect(jsonPath("$.message", CoreMatchers.is("Full authentication is required to access this resource")));

    }


    @Test
    public void shouldBeUnauthorized_WithInvalidToken() throws Exception {
        // Given
        long userId = 1L;

        UserDetails userDetails = new UserDetailsImpl(1L,"johndoe@gmail.com", "Doe", "John", true, "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW");

        String token =  Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();


        // When / Then
        mockMvc.perform(get("/api/user/" + userId)
                        .header("Authorization", "Bearer "+token+"invalid"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", CoreMatchers.is("Unauthorized")))
                .andExpect(jsonPath("$.message", CoreMatchers.is("Full authentication is required to access this resource")));

    }


    @Test
    public void shouldBeUnauthorized_WithMalformatedToken() throws Exception {
        // Given
        long userId = 1L;

        // When / Then
        mockMvc.perform(get("/api/user/" + userId)
                        .header("Authorization", "Bearer invalid"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", CoreMatchers.is("Unauthorized")))
                .andExpect(jsonPath("$.message", CoreMatchers.is("Full authentication is required to access this resource")));

    }

    @Test
    public void shouldBeUnauthorized_WithExpiredToken() throws Exception {
        // Given
        long userId = 1L;

        UserDetails userDetails = new UserDetailsImpl(1L,"johndoe@gmail.com", "Doe", "John", true, "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW");

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);
        String dateInString = "10-10-1980";
        Date date = formatter.parse(dateInString);
        String token = Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();


        // When / Then
        mockMvc.perform(get("/api/user/" + userId)
                        .header("Authorization", "Bearer "+token))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", CoreMatchers.is("Unauthorized")))
                .andExpect(jsonPath("$.message", CoreMatchers.is("Full authentication is required to access this resource")));

    }

    @Test
    public void shouldBeUnauthorized_WithUnsupportedToken() throws Exception {
        // Given
        long userId = 1L;

        UserDetails userDetails = new UserDetailsImpl(1L,"johndoe@gmail.com", "Doe", "John", true, "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW");


        String token = Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .compact();


        // When / Then
        mockMvc.perform(get("/api/user/" + userId)
                        .header("Authorization", "Bearer "+token))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", CoreMatchers.is("Unauthorized")))
                .andExpect(jsonPath("$.message", CoreMatchers.is("Full authentication is required to access this resource")));

    }

}
