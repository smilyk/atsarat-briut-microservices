package com.scheduler.exception;

public class SchedulerServiceException extends RuntimeException {
    private static final long serialVersionUID = -5932551679255676961L;

    public SchedulerServiceException(String message)
    {
        super(message);
    }
}
