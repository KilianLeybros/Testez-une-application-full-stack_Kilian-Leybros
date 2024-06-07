package com.openclassrooms.starterjwt.services.user;

import com.openclassrooms.starterjwt.data.UserList;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;
    private List<User> users;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
        users = UserList.initList();
    }

    @Test
    public void shouldDeleteAUser(){
        // Given
        Long userToDelete = 2L;
        willDoNothing().given(userRepository).deleteById(userToDelete);

        // When
        userService.delete(userToDelete);

        // Then
        verify(userRepository, times(1)).deleteById(userToDelete);
    }

    @Test
    public void shouldRetrieveAUser(){
        // Given
        Long userToRetrieve = 2L;
        when(userRepository.findById(userToRetrieve))
                .thenReturn(Optional.of(users.stream()
                        .filter(s -> s.getId().equals(userToRetrieve))
                        .findFirst()
                        .get()));


        // When
        User user = userService.findById(userToRetrieve);

        // Then
        Assertions.assertThat(user.getId()).isEqualTo(userToRetrieve);
        Assertions.assertThat(user.getFirstName()).isEqualTo("Bernard");
        Assertions.assertThat(user.getLastName()).isEqualTo("Jean");
        Assertions.assertThat(user.getEmail()).isEqualTo("jeanbernard@gmail.com");
    }
    @Test
    public void shouldReturnNull_WhenUserDoesntExist(){
        // Given
        Long userToRetrieve = 4L;
        when(userRepository.findById(userToRetrieve))
                .thenReturn(Optional.empty());


        // When
        User user = userService.findById(userToRetrieve);

        // Then
        org.junit.jupiter.api.Assertions.assertNull(user);
    }

}