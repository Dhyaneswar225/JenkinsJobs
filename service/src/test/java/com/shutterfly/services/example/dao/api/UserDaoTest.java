package com.shutterfly.services.example.dao.api;

import com.shutterfly.services.example.Application;
import com.shutterfly.services.example.exceptions.UserNotFoundException;
import com.shutterfly.services.example.model.internal.ProtectedUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * This test is more of an integration test which runs with the unit tests. It
 * stands up a spring-context and loads all of the framework classes. This includes
 * initializing and populating the H2 database, which is then used here.
 *
 * A true unit test would not use these class annotations (e.g., SpringJUnit4ClassRunner)
 * and instead it would mock out dependent components and then create a standard
 * object passing in the mocks as needed.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles(profiles = "test")
public class UserDaoTest {
    @Autowired
    public IUserDao userDao;

    @Test
    public void shouldRetrieveAllUsers() {
        // Checking whether the number of Users in the database is equal to the number specified here
        assertThat(userDao.retrieveUsers(), hasSize(2));
    }

    @Test
    public void shouldTestCreatedUsersData() {
        final ProtectedUser expectedFirstUserDetails = new ProtectedUser("testuser-1@shutterfly.com","testfname-1","1","localid-1");

        ProtectedUser actualUser = userDao.retrieveUsers().get(0);

        // Checking Details with values of the first User from the Database
        assertThat(actualUser, is(expectedFirstUserDetails));

    }

    @Test
    public void shouldThrowUserNotFoundException() throws Throwable {
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userDao.retrieveUserById("00");
        });
    }
}
