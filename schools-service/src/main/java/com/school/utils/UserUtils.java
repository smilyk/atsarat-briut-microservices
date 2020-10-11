package com.school.utils;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserUtils {

    public UUID generateUserId() {
        return UUID.randomUUID();
    }

}

