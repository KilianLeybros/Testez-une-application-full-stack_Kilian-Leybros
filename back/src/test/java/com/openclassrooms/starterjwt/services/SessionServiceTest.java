package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.data.SessionList;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class SessionServiceTest {
    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    private SessionService sessionService;

    private List<Session> sessions;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        sessionService = new SessionService(sessionRepository, userRepository);
        sessions = SessionList.initList();
    }


    @Test
    public void shouldRetrieveAllSessions(){
        // Given
        Mockito.when(sessionRepository.findAll()).thenReturn(sessions);

        // When
        List<Session> allSessions = sessionService.findAll();

        // Then
        Assertions.assertThat(allSessions)
                .extracting("id")
                .containsExactly(1L, 2L, 3L);
    }


    @Test
    public void shouldRetrieveSession(){
        // Given
        Long sessionToRetrieve = 2L;
        Mockito.when(sessionRepository.findById(sessionToRetrieve))
                .thenReturn(Optional.of(sessions.stream()
                        .filter(s -> s.getId().equals(sessionToRetrieve))
                        .findFirst()
                        .get()));

        // When
        Session retrievedSession = sessionService.getById(sessionToRetrieve);

        // Then
        Assertions.assertThat(retrievedSession.getId()).isEqualTo(sessionToRetrieve);
        Assertions.assertThat(retrievedSession.getName()).isEqualTo("session2");
        Assertions.assertThat(retrievedSession.getDescription()).isEqualTo("Une description2");
    }

    @Test
    public void shouldReturnNull_WhenSessionDoesNotExist(){
        // Given
        Long sessionToRetrieve = 4L;
        Mockito.when(sessionRepository.findById(sessionToRetrieve)).thenReturn(Optional.empty());

        // When
        Session retrievedSession = sessionService.getById(sessionToRetrieve);

        // Then
        org.junit.jupiter.api.Assertions.assertNull(retrievedSession);
    }

    @Test
    public void shouldCreateSession(){
        // Given
        Session sessionToCreate = new Session(
                4L,
                "Une session4",
                Date.from(Instant.now()),
                "Une description4",
                new Teacher(), null,
                LocalDateTime.now(),
                null);
        Mockito.when(sessionRepository.save(sessionToCreate)).then(s ->{
            sessions.add(s.getArgument(0));
            return sessionToCreate;
        });


        // When
        Session createdSession = sessionService.create(sessionToCreate);

        // Then
        Assertions.assertThat(sessions).contains(sessionToCreate);

    }

    @Test
    public void shouldUpdateSession(){
        // Given
        Long updatedSessionId = 1L;
        LocalDateTime updatedDate = LocalDateTime.now();
        Session sessionToUpdate = new Session(
                null,
                "Une session modifiée",
                Date.from(Instant.now()),
                "Une description modifiée",
                new Teacher(), null,
                LocalDateTime.now(),
                updatedDate);
        Mockito.when(sessionRepository.save(sessionToUpdate)).then(s ->{
            sessions.set(0,s.getArgument(0));
            return sessionToUpdate;
        });


        // When
        Session updatedSession = sessionService.update(updatedSessionId, sessionToUpdate);

        // Then
        Assertions.assertThat(sessions).contains(updatedSession);
        Assertions.assertThat(updatedSession.getUpdatedAt()).isEqualTo(updatedDate);
        Assertions.assertThat(updatedSession.getName()).isEqualTo("Une session modifiée");
    }
}
