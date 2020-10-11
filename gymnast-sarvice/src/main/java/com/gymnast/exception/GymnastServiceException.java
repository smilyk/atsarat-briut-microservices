package com.gymnast.exception;

public class GymnastServiceException extends RuntimeException {
    private static final long serialVersionUID = -8885406797545423783L;

    public GymnastServiceException(String message)
    {
        super(message);
    }
}
