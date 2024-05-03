package com.openclassrooms.starterjwt.services.teacher;
import com.openclassrooms.starterjwt.data.TeacherList;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    private TeacherService teacherService;
    private List<Teacher> teachers;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        teacherService = new TeacherService(teacherRepository);
        teachers = TeacherList.initList();
    }

    @Test
    public void shouldRetrieveAllTeachers(){
        // Given
        when(teacherRepository.findAll()).thenReturn(teachers);

        // When
        List<Teacher> allTeachers = teacherService.findAll();

        // Then
        Assertions.assertThat(allTeachers)
                .extracting("firstName")
                .containsExactly("Francois", "Bernard", "Dumont");
    }
    @Test
    public void shouldRetrieveATeacher(){
        // Given
        Long teacherToRetrieve = 2L;
        when(teacherRepository.findById(teacherToRetrieve))
                .thenReturn(Optional.of(teachers.stream()
                        .filter(s -> s.getId().equals(teacherToRetrieve))
                        .findFirst()
                        .get()));


        // When
        Teacher teacher = teacherService.findById(teacherToRetrieve);

        // Then
        Assertions.assertThat(teacher.getId()).isEqualTo(teacherToRetrieve);
        Assertions.assertThat(teacher.getFirstName()).isEqualTo("Bernard");
        Assertions.assertThat(teacher.getLastName()).isEqualTo("Jean");
    }
    @Test
    public void shouldReturnNull_WhenTeacherDoesntExist(){
        // Given
        Long teacherToRetrieve = 4L;
        when(teacherRepository.findById(teacherToRetrieve))
                .thenReturn(Optional.empty());


        // When
        Teacher teacher = teacherService.findById(teacherToRetrieve);

        // Then
        org.junit.jupiter.api.Assertions.assertNull(teacher);
    }

}