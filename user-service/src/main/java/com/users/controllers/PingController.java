package com.users.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
@RestController
@RequestMapping("/users/v1/ping")
@RefreshScope
public class PingController {
    private String currentDate = LocalDateTime.now().toLocalDate().toString();

    @Value("${tokenSecret}")
    String port;

    @GetMapping()
    public String ping() {

        return " Users-Service working " + currentDate + " secret word is: " + port;
    }
}
