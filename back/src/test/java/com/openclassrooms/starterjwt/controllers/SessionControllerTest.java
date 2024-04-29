package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.data.SessionList;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    SessionService sessionService;
    @MockBean
    SessionMapper sessionMapper;

    private List<Session> sessions;

    private List<SessionDto> sessionsDto;
    @Test
    @WithMockUser
    public void shouldListAllSessions() throws Exception {
        // Given
        sessions = SessionList.initList();
        sessionsDto = SessionList.initDtoList();

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionsDto);

        // When / Then
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", CoreMatchers.is("session1")))
                .andExpect(jsonPath("$[1].name", CoreMatchers.is("session2")))
                .andExpect(jsonPath("$[2].name", CoreMatchers.is("session3")));

    }

    @Test
    @WithMockUser
    public void shouldRetrieveSession() throws Exception {
        // Given
        Long sessionId = 1L;
        Session session = new Session(1L, "session1", Date.from(Instant.now()), "Une description", new Teacher(), null, LocalDateTime.now(), LocalDateTime.now());
        SessionDto sessionDto = new SessionDto(1L, "session1", Date.from(Instant.now()), null, "Une description", null , LocalDateTime.now(), LocalDateTime.now());
        when(sessionService.getById(sessionId)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // When / Then
        mockMvc.perform(get("/api/session/" + sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is("session1")));

    }

    @Test
    @WithMockUser
    public void shouldCreateSession() throws Exception {
        // Given
        SessionDto sessionDto = new SessionDto(null, "Nouvelle session", Date.from(Instant.now()), 1L, "Une nouvelle description", null , LocalDateTime.now(), null);
        Session session = new Session(null, "Nouvelle session", Date.from(Instant.now()), "Une nouvelle description", new Teacher(), null, LocalDateTime.now(), null);

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);

        // When / Then
        mockMvc.perform(post("/api/session/")
                        .content(asJsonString(sessionDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name", CoreMatchers.is("Nouvelle session")));
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
