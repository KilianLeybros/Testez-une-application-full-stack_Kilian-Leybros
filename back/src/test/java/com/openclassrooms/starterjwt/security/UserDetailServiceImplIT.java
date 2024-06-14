package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDetailServiceImplIT {

    @Mock
    private UserRepository userRepository;


    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void shouldLoadUserByUsername(){
        // Given
        String username = "johndoe2@gmail.com";

        // When
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        // Then
        org.junit.jupiter.api.Assertions.assertNotNull(userDetails);
        Assertions.assertThat(userDetails.getFirstName()).isEqualTo("John2");
        Assertions.assertThat(userDetails.getLastName()).isEqualTo("Doe2");
        Assertions.assertThat(userDetails.getUsername()).isEqualTo(username);
    }

    @Test
    public void shouldFailToLoadUserByUsername_WhenUserDoesntExist (){
        // Given
        String username = "email@noexist.com";

        // When / Then
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
             userDetailsService.loadUserByUsername(username);
        }).isInstanceOf(UsernameNotFoundException.class);
    }
}
