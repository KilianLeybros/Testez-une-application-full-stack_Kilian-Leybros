package com.openclassrooms.starterjwt.services.teacher;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TeacherIntegrationServiceTest {

    @Autowired
    private TeacherService teacherService;



    @Test
    public void shouldRetrieveAllTeachers(){
        // When
        List<Teacher> allTeachers = teacherService.findAll();

        // Then
        org.junit.jupiter.api.Assertions.assertNotNull(allTeachers);
        Assertions.assertThat(allTeachers)
                .extracting("firstName")
                .containsExactly("JohnTeacher", "JohnTeacher2");
    }
    @Test
    public void shouldRetrieveATeacher(){
        // Given
        Long teacherToRetrieve = 2L;

        // When
        Teacher teacher = teacherService.findById(teacherToRetrieve);

        // Then
        org.junit.jupiter.api.Assertions.assertNotNull(teacher);
        Assertions.assertThat(teacher.getFirstName()).isEqualTo("JohnTeacher2");
        Assertions.assertThat(teacher.getLastName()).isEqualTo("DoeTeacher2");
    }
    @Test
    public void shouldReturnNull_WhenTeacherDoesntExist(){
        // Given
        Long teacherToRetrieve = 4L;

        // When
        Teacher teacher = teacherService.findById(teacherToRetrieve);

        // Then
        org.junit.jupiter.api.Assertions.assertNull(teacher);
    }

}