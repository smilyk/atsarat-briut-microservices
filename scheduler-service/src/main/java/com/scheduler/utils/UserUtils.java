package com.scheduler.utils;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserUtils {

    public UUID generateUUID() {
        return UUID.randomUUID();
    }



}

