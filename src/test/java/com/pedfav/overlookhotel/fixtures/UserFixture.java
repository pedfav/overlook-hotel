package com.pedfav.overlookhotel.fixtures;

import com.pedfav.overlookhotel.entities.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class UserFixture {

    public static User user() {
        return User.builder()
                .id(1L)
                .name("Jack Torrance")
                .email("jack@gmail.com")
                .birthday(LocalDate.of(1940, 10, 10))
                .build();
    }

    public static List<User> userList() {
        return Arrays.asList(
                User.builder()
                        .id(1L)
                        .name("Jack Torrance")
                        .email("jack@gmail.com")
                        .birthday(LocalDate.of(1940, 10, 10))
                        .build(),
                User.builder()
                        .id(1L)
                        .name("Wendy Torrance")
                        .email("wendy@gmail.com")
                        .birthday(LocalDate.of(1941, 9, 9))
                        .build(),
                User.builder()
                        .id(1L)
                        .name("Danny Torrance")
                        .email("danny@gmail.com")
                        .birthday(LocalDate.of(1943, 9, 9))
                        .build()
        );
    }
}
