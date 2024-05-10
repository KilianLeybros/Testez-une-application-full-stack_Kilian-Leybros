package com.openclassrooms.starterjwt.data;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SessionList {


    public static List<Session> initList(){
        List<User> users = new ArrayList<User>();
        List<Session> sessions = new ArrayList<Session>();

        Session session1 = new Session(1L, "session1", Date.from(Instant.now()), "Une description", new Teacher(), users, LocalDateTime.now(), LocalDateTime.now());
        sessions.add(session1);

        Session session2 = new Session(2L, "session2", Date.from(Instant.now()), "Une description2", new Teacher(), users, LocalDateTime.now(), LocalDateTime.now());
        sessions.add(session2);

        Session session3 = new Session(3L, "session3", Date.from(Instant.now()), "Une description3", new Teacher(), users, LocalDateTime.now(), LocalDateTime.now());
        sessions.add(session3);

        return sessions;
    }

    public static List<SessionDto> initDtoList(){
        List<Long> users = new ArrayList<Long>();
        List<SessionDto> sessionsDto = new ArrayList<SessionDto>();

        SessionDto sessionDto1 = new SessionDto(1L, "session1", Date.from(Instant.now()), null, "Une description", users , LocalDateTime.now(), LocalDateTime.now());
        sessionsDto.add(sessionDto1);

        SessionDto sessionDto2 = new SessionDto(1L, "session2", Date.from(Instant.now()), null, "Une description2", users , LocalDateTime.now(), LocalDateTime.now());
        sessionsDto.add(sessionDto2);

        SessionDto sessionDto3 = new SessionDto(1L, "session3", Date.from(Instant.now()), null, "Une description3", users , LocalDateTime.now(), LocalDateTime.now());
        sessionsDto.add(sessionDto3);

        return sessionsDto;
    }
}
