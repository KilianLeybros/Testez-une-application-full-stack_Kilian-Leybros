package com.openclassrooms.starterjwt.controllers.teacher;

import com.openclassrooms.starterjwt.data.TeacherList;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TeacherService teacherService;
    @MockBean
    TeacherMapper teacherMapper;

    private List<Teacher> teachers;

    private List<TeacherDto> teacherDtos;

    @Test
    @WithMockUser
    public void shouldListAllTeachers() throws Exception {
        // Given
        teachers = TeacherList.initList();
        teacherDtos = TeacherList.initDtoList();

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        // When / Then
        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].firstName", CoreMatchers.is("Francois")))
                .andExpect(jsonPath("$[1].firstName", CoreMatchers.is("Bernard")))
                .andExpect(jsonPath("$[2].firstName", CoreMatchers.is("Dumont")));

    }

    @Test
    @WithMockUser
    public void shouldRetrieveTeacher() throws Exception {
        // Given
        long teacherId = 1L;
        Teacher teacher =  new Teacher(1L, "Jean", "Francois",  LocalDateTime.now(), LocalDateTime.now());
        TeacherDto teacherDto = new TeacherDto(1L, "Jean", "Francois",  LocalDateTime.now(), LocalDateTime.now());
        when(teacherService.findById(teacherId)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        // When / Then
        mockMvc.perform(get("/api/teacher/" + teacherId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", CoreMatchers.is("Jean")))
                .andExpect(jsonPath("$.firstName", CoreMatchers.is("Francois")));

    }

    @Test
    @WithMockUser
    public void shouldFailToRetrieveTeacher404_WhenTeacherNotFound() throws Exception {
        // Given
        long teacherId = 999L;
        when(teacherService.findById(teacherId)).thenReturn(null);

        // When / Then
        mockMvc.perform(get("/api/teacher/" + teacherId))
                .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser
    public void shouldFailToRetrieveTeacher400_WhenTeacherIdIsNotANumber() throws Exception {
        // Given
        String teacherId = "AAA";

        // When / Then
        mockMvc.perform(get("/api/teacher/" + teacherId))
                .andExpect(status().isBadRequest());

    }
}
