/*
 * Copyright (c) Shutterfly.com, Inc. 2019. All Rights Reserved.
 */

package com.shutterfly.services.example.model;

public class User {
    private String userID;
    private String userName;
    private String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (userID != null ? !userID.equals(user.userID) : user.userID != null) {
            return false;
        }
        if (userName != null ? !userName.equals(user.userName) : user.userName != null) {
            return false;
        }
        return !(userEmail != null ? !userEmail.equals(user.userEmail) : user.userEmail != null);

    }

    @Override
    public int hashCode() {
        int result = userID != null ? userID.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
               "userID='" + userID + '\'' +
               ", userName='" + userName + '\'' +
               ", userEmail='" + userEmail + '\'' +
               '}';
    }
}
