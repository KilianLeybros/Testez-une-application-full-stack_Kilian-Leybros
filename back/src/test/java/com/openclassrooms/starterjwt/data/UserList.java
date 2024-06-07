package com.openclassrooms.starterjwt.data;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserList {
    public static List<User> initList(){

        List<User> users = new ArrayList<User>();
        //password = a1b2c3
        User user1 = new User(1L,"jeanfrancois@gmail.com", "Jean", "Francois",  "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW", true, LocalDateTime.now(), null);
        users.add(user1);

        User user2 = new User(2L,"jeanbernard@gmail.com", "Jean", "Bernard",  "$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW", false, LocalDateTime.now(), null);
        users.add(user2);


        return users;
    }

    public static List<UserDto> initDtoList(){

        List<UserDto> userDtos = new ArrayList<UserDto>();
        //password = a1b2c3
        UserDto user1 = new UserDto(1L,"jeanfrancois@gmail.com", "Jean", "Francois",   true,"$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW", LocalDateTime.now(), null);
        userDtos.add(user1);

        UserDto user2 = new UserDto(2L,"jeanbernard@gmail.com", "Jean", "Bernard",  false,"$2a$10$b0g598cb8kBqzob940yw1Oz7aXBtOyYvn1KRPO5YEi3858IsB2GnW", LocalDateTime.now(), null);
        userDtos.add(user2);

        return userDtos;
    }
}
