/*
 * Copyright (c) Shutterfly.com, Inc. 2015-2017. All Rights Reserved.
 */
package com.shutterfly.services.example.exceptions;

import com.shutterfly.springboard.common.exception.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
