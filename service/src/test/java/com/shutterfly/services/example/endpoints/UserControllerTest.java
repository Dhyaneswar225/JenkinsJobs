/*
 * Copyright (c) Shutterfly.com, Inc. 2015-2017. All Rights Reserved.
 */
package com.shutterfly.services.example.endpoints;

import com.shutterfly.services.example.exceptions.UserNotFoundException;
import com.shutterfly.services.example.model.User;
import com.shutterfly.services.example.services.api.IUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests with mocks
 */
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private IUserService userService;

    @Test
    public void testGetUser() {
        User user = mock(User.class);
        when(user.getUserEmail()).thenReturn("test@test.com");
        when(userService.findUserById("1")).thenReturn(user);

        UserController userController = new UserController(userService);

        User fetchedUser = userController.userById("1");
        Assertions.assertEquals("test@test.com", fetchedUser.getUserEmail());
    }


    @Test
    public void testMissingUser() {
        Assertions.assertThrows(UserNotFoundException.class,() -> {
            when(userService.findUserById("2")).thenThrow(new UserNotFoundException("not found", new RuntimeException("cause")));

            UserController userController = new UserController(userService);

            userController.userById("2");
        });

    }

    @Disabled("Enable this test when searching by email is implemented.")
    @Test
    public void testGetUserByEmail() {
        User user = mock(User.class);
        when(user.getUserEmail()).thenReturn("test@test.com");
        //when(userService.findUserByEmail("1")).thenReturn(user);

        UserController userController = new UserController(userService);

        // TODO: Implement. Something like: userController.users("test@test.com");
        User fetchedUser = null;
        Assertions.assertEquals("1", fetchedUser.getUserID());
    }
}
