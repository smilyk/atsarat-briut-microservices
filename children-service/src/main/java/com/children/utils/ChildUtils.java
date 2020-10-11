package com.children.utils;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChildUtils {

    public UUID generateUserId() {
        return UUID.randomUUID();
    }

}
