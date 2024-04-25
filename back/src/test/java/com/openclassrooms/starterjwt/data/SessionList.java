package com.openclassrooms.starterjwt.data;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
}
