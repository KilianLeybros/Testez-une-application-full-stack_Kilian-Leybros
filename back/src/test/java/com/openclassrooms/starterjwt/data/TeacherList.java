package com.openclassrooms.starterjwt.data;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeacherList {
    public static List<Teacher> initList(){

        List<Teacher> teachers = new ArrayList<Teacher>();

        Teacher teacher1 = new Teacher(1L, "Jean", "Francois",  LocalDateTime.now(), LocalDateTime.now());
        teachers.add(teacher1);

        Teacher teacher2 = new Teacher(2L, "Jean", "Bernard",  LocalDateTime.now(), LocalDateTime.now());
        teachers.add(teacher2);

        Teacher teacher3 = new Teacher(3L, "Jean", "Dumont",  LocalDateTime.now(), LocalDateTime.now());
        teachers.add(teacher3);

        return teachers;
    }

    public static List<TeacherDto> initDtoList(){
        List<Long> users = new ArrayList<Long>();
        List<TeacherDto> teacherDtos = new ArrayList<TeacherDto>();

        TeacherDto teacherDto1 = new TeacherDto(1L, "Jean", "Francois",  LocalDateTime.now(), LocalDateTime.now());
        teacherDtos.add(teacherDto1);

        TeacherDto teacherDto2 = new TeacherDto(2L, "Jean", "Bernard",  LocalDateTime.now(), LocalDateTime.now());
        teacherDtos.add(teacherDto2);

        TeacherDto teacherDto3 = new TeacherDto(3L, "Jean", "Dumont",  LocalDateTime.now(), LocalDateTime.now());
        teacherDtos.add(teacherDto3);

        return teacherDtos;
    }
}
