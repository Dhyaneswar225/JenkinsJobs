package com.shutterfly.services.example.dao.api;

import com.shutterfly.services.example.model.internal.ProtectedUser;

import java.util.List;

public interface IUserDao {
    List<ProtectedUser> retrieveUsers();
    ProtectedUser retrieveUserById(String id);
}
