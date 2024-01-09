/*
 * Copyright (c) Shutterfly.com, Inc. 2019. All Rights Reserved.
 */
package com.shutterfly.services.example.model.internal;

import java.io.Serializable;

/**
 * Implementation of a protected user.
 */
public class ProtectedUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String userEmail;
    private final String userName;
    private final String userID;
    private final String userLocalID;

    public ProtectedUser(final String userEmail, final String userName, final String userID, final String userLocalID) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userID = userID;
        this.userLocalID = userLocalID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserLocalID() {
        return userLocalID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProtectedUser)) {
            return false;
        }

        ProtectedUser that = (ProtectedUser) o;

        if (userEmail != null ? !userEmail.equals(that.userEmail) : that.userEmail != null) {
            return false;
        }
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) {
            return false;
        }
        if (userID != null ? !userID.equals(that.userID) : that.userID != null) {
            return false;
        }
        return !(userLocalID != null ? !userLocalID.equals(that.userLocalID)
                                     : that.userLocalID != null);

    }

    @Override
    public int hashCode() {
        int result = userEmail != null ? userEmail.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (userID != null ? userID.hashCode() : 0);
        result = 31 * result + (userLocalID != null ? userLocalID.hashCode() : 0);
        return result;
    }
}
