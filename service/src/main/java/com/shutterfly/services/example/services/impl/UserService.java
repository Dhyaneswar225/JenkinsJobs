/*
 * Copyright (c) Shutterfly.com, Inc. 2015-2017. All Rights Reserved.
 */
package com.shutterfly.services.example.services.impl;

import com.shutterfly.services.example.dao.api.IUserDao;
import com.shutterfly.services.example.model.User;
import com.shutterfly.services.example.model.internal.ProtectedUser;
import com.shutterfly.services.example.services.api.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the UserService.
 */
@Service
public class UserService implements IUserService {
    private final IUserDao userDao;

    @Autowired
    public UserService(final IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> findAllUsers() {
        return adaptAll(userDao.retrieveUsers());
    }

    @Override
    public User findUserById(String userId) {
        ProtectedUser user = userDao.retrieveUserById(userId);
        return adapt(user);
    }

    private User adapt(ProtectedUser protectedUser) {
        User adaptedUser = new User();
        adaptedUser.setUserEmail(protectedUser.getUserEmail());
        adaptedUser.setUserName(protectedUser.getUserName());
        adaptedUser.setUserID(protectedUser.getUserID());
        return adaptedUser;
    }

    private List<User> adaptAll(List<ProtectedUser> protectedUsers) {
        return protectedUsers.stream()
                .map(this::adapt)
                .collect(Collectors.toList());
    }
}
