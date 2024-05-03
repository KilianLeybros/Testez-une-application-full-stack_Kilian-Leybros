package com.openclassrooms.starterjwt.controllers.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    ObjectMapper objectMapper = new ObjectMapper()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new JavaTimeModule());

    ObjectWriter objectWriter = objectMapper.writer();
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
        long sessionId = 1L;
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
    public void shouldFailToRetrieveSession404_WhenSessionNotFound() throws Exception {
        // Given
        long sessionId = 999L;
        when(sessionService.getById(sessionId)).thenReturn(null);

        // When / Then
        mockMvc.perform(get("/api/session/" + sessionId))
                .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser
    public void shouldFailToRetrieveSession400_WhenSessionIdIsNotANumber() throws Exception {
        // Given
        String sessionId = "AAA";

        // When / Then
        mockMvc.perform(get("/api/session/" + sessionId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void shouldCreateSession() throws Exception {
        // Given
        SessionDto sessionDto = new SessionDto(null, "Nouvelle session", Date.from(Instant.now()), 1L, "Une nouvelle description", null , LocalDateTime.now(), null);
        Session session = new Session(null, "Nouvelle session", Date.from(Instant.now()), "Une nouvelle description", new Teacher(), null, LocalDateTime.now(), null);

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        String content = objectWriter.writeValueAsString(sessionDto);

        // When / Then
        mockMvc.perform(post("/api/session/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name", CoreMatchers.is("Nouvelle session")));
    }


    @Test
    @WithMockUser
    public void shouldUpdateSession() throws Exception {
        // Given
        long sessionId = 1L;
        SessionDto sessionDto = new SessionDto(null, "Updated session", Date.from(Instant.now()), 1L, "Updated description", null , LocalDateTime.now(), null);
        Session session = new Session(null, "Updated session", Date.from(Instant.now()), "Updated description", new Teacher(), null, LocalDateTime.now(), null);

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(sessionId ,session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        String content = objectWriter.writeValueAsString(sessionDto);

        // When / Then
        mockMvc.perform(put("/api/session/" + sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name", CoreMatchers.is("Updated session")));
    }

    @Test
    @WithMockUser
    public void shouldFailToUpdate400_WhenSessionIdIsNotANumber() throws Exception {
        // Given
        String sessionId = "AAA";
        SessionDto sessionDto = new SessionDto(null, "Updated session", Date.from(Instant.now()), 1L, "Updated description", null , LocalDateTime.now(), null);


        // When / Then
        mockMvc.perform(put("/api/session/" + sessionId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void shouldFailToUpdateSession400_WhenSessionIdNotANumber() throws Exception {
        // Given
        String sessionId = "AAA";

        // When / Then
        mockMvc.perform(put("/api/session/" + sessionId))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser
    public void shouldDeleteSession() throws Exception {
        // Given
        long sessionId = 1L;
        Session session = new Session(1L, "Session to Delete", Date.from(Instant.now()), "Deleted description", new Teacher(), null, LocalDateTime.now(), null);
        when(sessionService.getById(sessionId)).thenReturn(session);

        // When / Then
        mockMvc.perform(delete("/api/session/" + sessionId))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    public void shouldFailToDeleteSession404_WhenSessionNotFound() throws Exception {
        // Given
        Long sessionId = 999L;
        when(sessionService.getById(sessionId)).thenReturn(null);

        // When / Then
        mockMvc.perform(delete("/api/session/" + sessionId))
                .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser
    public void shouldFailToDeleteSession400_WhenSessionIdIsNotANumber() throws Exception {
        // Given
        String sessionId = "AAA";

        // When / Then
        mockMvc.perform(delete("/api/session/" + sessionId))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser
    public void shouldParticipateToSession() throws Exception {
        // Given
        long sessionId = 1L;
        long userId = 1L;


        // When / Then
        mockMvc.perform(post("/api/session/" + sessionId + "/participate/" + userId))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    public void shouldFailToParticipateToSession400_WhenSessionIdIsNotANumber() throws Exception {
        // Given
        String sessionId = "AAA";
        long userId = 1L;


        // When / Then
        mockMvc.perform(post("/api/session/" + sessionId + "/participate/" + userId))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser
    public void shouldFailToParticipateToSession400_WhenUserIdIsNotANumber() throws Exception {
        // Given
        long sessionId = 1L;
        String userId = "AAA";


        // When / Then
        mockMvc.perform(post("/api/session/" + sessionId + "/participate/" + userId))
                .andExpect(status().isBadRequest());

    }


    @Test
    @WithMockUser
    public void shouldNoLongerParticipateToSession() throws Exception {
        // Given
        long sessionId = 1L;
        long userId = 1L;


        // When / Then
        mockMvc.perform(delete("/api/session/" + sessionId + "/participate/" + userId))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    public void shouldFailToNoLongerParticipateToSession400_WhenSessionIdIsNotANumber() throws Exception {
        // Given
        String sessionId = "AAA";
        long userId = 1L;


        // When / Then
        mockMvc.perform(delete("/api/session/" + sessionId + "/participate/" + userId))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser
    public void shouldFailToNoLongerParticipateToSession400_WhenUserIdIsNotANumber() throws Exception {
        // Given
        long sessionId = 1L;
        String userId = "AAA";


        // When / Then
        mockMvc.perform(delete("/api/session/" + sessionId + "/participate/" + userId))
                .andExpect(status().isBadRequest());

    }

}
