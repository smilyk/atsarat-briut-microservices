package com.school.controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/school/v1/ping")
public class PingController {
    private String currentDate = LocalDateTime.now().toLocalDate().toString();


    @GetMapping()
    public String ping() {
        return " School-Service working " + currentDate;
    }
}
