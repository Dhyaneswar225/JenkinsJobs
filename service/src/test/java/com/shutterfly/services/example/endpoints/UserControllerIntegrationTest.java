/*
 * Copyright (c) Shutterfly.com, Inc. 2015-2017. All Rights Reserved.
 */
package com.shutterfly.services.example.endpoints;

import com.shutterfly.services.example.Application;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests that mock spring
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getUsersStatus() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserById() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userID", is("1")))
                .andExpect(jsonPath("$.userEmail", is("testuser-1@shutterfly.com")));
    }

    @Disabled("As an exercise make this test pass.")
    @Test
    public void getUserByEmail() throws Exception {
        mockMvc.perform(get("/users?email=testuser-1@shutterfly.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userID", is("1")));
    }
}
