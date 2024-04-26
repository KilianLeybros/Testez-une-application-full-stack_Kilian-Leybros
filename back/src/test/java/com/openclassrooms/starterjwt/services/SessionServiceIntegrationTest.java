package com.openclassrooms.starterjwt.services;


import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SessionServiceIntegrationTest {

    @Autowired
    private SessionService sessionService;

    @Test
    public void shouldRetrieveAllSessions(){
        // When
        List<Session> sessionList = sessionService.findAll();

        // Then
        Assertions.assertNotNull(sessionList);
    }

    @Test
    public void shouldRetrieveSession(){
        // Given
        Long sessionId = 1L;

        // When
        Session session = sessionService.getById(sessionId);

        // Then
        Assertions.assertNotNull(session);
        org.assertj.core.api.Assertions.assertThat(session.getName()).isEqualTo("Session1");
    }

    @Test
    public void shouldReturnNull_whenSessionNotExist(){
        // Given
        Long sessionId = 999L;

        // When
        Session session = sessionService.getById(sessionId);

        // Then
        Assertions.assertNull(session);
    }


    @Test
    public void shouldCreateSession(){
        // Given
        Session sessionToCreate = new Session(
                3L,
                "Session3",
                Date.from(Instant.now()),
                "Description3",
                null, null,
                LocalDateTime.now(),
                null);

        // When
        sessionService.create(sessionToCreate);
        Session createdSession = sessionService.getById(sessionToCreate.getId());

        // Then
        Assertions.assertNotNull(createdSession);
        org.assertj.core.api.Assertions.assertThat(createdSession.getName()).isEqualTo("Session3");
        org.assertj.core.api.Assertions.assertThat(createdSession.getDescription()).isEqualTo("Description3");

    }

    @Test
    public void shouldUpdateSession(){
        // Given
        LocalDateTime updatedDate = LocalDateTime.now();
        Session sessionToUpdate = new Session(
                null,
                "UpdatedSession",
                Date.from(Instant.now()),
                "UpdatedDescription",
                null, null,
                LocalDateTime.now(),
                updatedDate);

        // When
        sessionService.update(1L, sessionToUpdate);
        Session updatedSession = sessionService.getById(1L);

        // Then
        org.assertj.core.api.Assertions.assertThat(updatedSession.getName()).isEqualTo("UpdatedSession");
        org.assertj.core.api.Assertions.assertThat(updatedSession.getDescription()).isEqualTo("UpdatedDescription");
        org.assertj.core.api.Assertions.assertThat(updatedSession.getUpdatedAt().toLocalDate()).isEqualTo(updatedDate.toLocalDate());

    }

    @Test
    public void shouldDeleteSession(){
        // Given
        Long sessionId = 1L;

        // When
        sessionService.delete(sessionId);
        List<Session> sessions = sessionService.findAll();

        // Then
        org.assertj.core.api.Assertions.assertThat(sessions)
                .extracting("id")
                .containsExactly(2L);
    }

    @Test
    public void shouldFailToDelete_WhenSessionDoesNotExist(){
        // Given
        Long id = 999L;

        // When / Then
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
            sessionService.delete(id);
        }).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    public void shouldParticipateToSession(){
        // Given
        Long sessionId = 1L;
        Long userId = 3L;

        // When
        sessionService.participate(sessionId, userId);
        Session session = sessionService.getById(sessionId);

        // Then
        org.assertj.core.api.Assertions.assertThat(session.getUsers()).extracting("id")
                .containsExactly(1L, 2L, 3L);
    }

    @Test
    public void shouldNoLongerParticipateToSession(){
        // Given
        Long sessionId = 1L;
        Long userId = 2L;

        // When
        sessionService.noLongerParticipate(sessionId, userId);
        Session session = sessionService.getById(sessionId);

        // Then
        org.assertj.core.api.Assertions.assertThat(session.getUsers()).extracting("id")
                .containsExactly(1L);
    }


    @Test
    public void shouldFailToParticipateToSession_WhenAlreadyParticipate(){
        // Given
        Long sessionId = 1L;
        Long userId = 2L;

        // When / Then
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
            sessionService.participate(sessionId, userId);
        }).isInstanceOf(BadRequestException.class);
    }

    @Test
    public void shouldFailToNoLongerParticipateToSession_WhenAlreadyNoParticipate(){
        // Given
        Long sessionId = 1L;
        Long userId = 3L;

        // When / Then
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
            sessionService.noLongerParticipate(sessionId, userId);
        }).isInstanceOf(BadRequestException.class);
    }
}
