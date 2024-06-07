package com.openclassrooms.starterjwt.services.user;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;

import static org.mockito.Mockito.verify;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceIT {

    @Autowired
    private UserService userService;


    @Test
    public void shouldRetrieveAUser(){
        // Given
        Long userToRetrieve = 3L;

        // When
        User user = userService.findById(userToRetrieve);

        // Then
        org.junit.jupiter.api.Assertions.assertNotNull(user);
        Assertions.assertThat(user.getFirstName()).isEqualTo("John3");
        Assertions.assertThat(user.getLastName()).isEqualTo("Doe3");
        Assertions.assertThat(user.getEmail()).isEqualTo("johndoe3@gmail.com");
    }
    @Test
    public void shouldReturnNull_WhenUserDoesntExist(){
        // Given
        Long userToRetrieve = 999L;

        // When
        User user = userService.findById(userToRetrieve);

        // Then
        org.junit.jupiter.api.Assertions.assertNull(user);
    }

    @Test
    public void shouldDeleteAUser(){
        // Given
        Long userToDelete = 4L;

        // When
        userService.delete(userToDelete);

        // Then
        User user = userService.findById(userToDelete);
        org.junit.jupiter.api.Assertions.assertNull(user);
    }

    @Test
    public void shouldFailToDeleteAUser_WhenUserParticipateToSession(){
        // Given
        Long userToDelete = 3L;

        // When / Then
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
            userService.delete(userToDelete);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

}