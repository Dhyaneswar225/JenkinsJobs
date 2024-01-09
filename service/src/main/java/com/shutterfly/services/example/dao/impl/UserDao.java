package com.shutterfly.services.example.dao.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.shutterfly.services.example.dao.api.IUserDao;
import com.shutterfly.services.example.exceptions.UserNotFoundException;
import com.shutterfly.services.example.model.internal.ProtectedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserDao implements IUserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);

    private static final List<ProtectedUser> USER_ROW_MAPPER = Arrays.asList(
            new ProtectedUser("testuser-1@shutterfly.com","testfname-1","1","localid-1"),
            new ProtectedUser("testuser-2@shutterfly.com","testfname-2","2","localid-2"));

    // The Spring Cacheable annotation is used here to transparently cache the
    // return value of the method. Any arguments to the method are used when
    // building the key for the cache. See the configuration under
    // service.cache in the yaml file for settings. The name of the cache needs
    // to match the name of one of the caches in the configuration
    @Override
    @Cacheable("example_users")
    @HystrixCommand
    public List<ProtectedUser> retrieveUsers() {
        LOGGER.info("Fetching users from database");
        return USER_ROW_MAPPER;
    }

    @Override
    @Cacheable("example_users")
    @HystrixCommand
    public ProtectedUser retrieveUserById(final String uid) {
        LOGGER.info("Fetching user [{}] from database", uid);
        try {
            return USER_ROW_MAPPER.stream().filter(protectedUser ->
                    protectedUser.getUserID().equals(uid)).findAny().orElseThrow(Exception::new);
        } catch (Exception ex) {
            throw new UserNotFoundException("User Not Found", ex);
        }
    }
}
