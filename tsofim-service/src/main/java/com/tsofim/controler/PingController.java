package com.tsofim.controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/tsofiml/v1/ping")
public class PingController {
    private String currentDate = LocalDateTime.now().toLocalDate().toString();


    @GetMapping()
    public String ping() {
        return " Tsofim-Service working " + currentDate;
    }
}
