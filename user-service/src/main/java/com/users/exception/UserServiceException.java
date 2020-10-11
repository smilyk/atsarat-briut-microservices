package com.users.exception;

public class UserServiceException extends RuntimeException {
    private static final long serialVersionUID = -5932551679255676961L;

    public UserServiceException(String message)
    {
        super(message);
    }
}
