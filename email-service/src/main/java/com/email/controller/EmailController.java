package com.email.controller;

import com.email.dto.EmailDto;
import com.email.dto.EmailVerificationDto;
import com.email.services.emailServices.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    EmailService emailService;


    @PostMapping("/verification-email")
    String sendSimpleEmail(@RequestBody EmailVerificationDto mail)  {
        return emailService.sendRegistrationEmail(mail);
    }

    @PostMapping("/gymnast-email")
    String sendGymnastEmail(@RequestBody EmailDto mail)  {
        return emailService.sendGymnastEmail(mail);

    }

    @PostMapping("/ben-gurion-email")
    String sendSchoolEmail(@RequestBody EmailDto mail){
        return emailService.sendSchoolEmail(mail);

    }

    @PostMapping("/tsofim-email")
    String sendTsofimEmail(@RequestBody EmailDto mail)  {
        return emailService.sendTsofimEmail(mail);

    }

    private void emailError(String email, String service, String userFirstName, String userLastName){
        emailService.emailError(email, service, userFirstName, userLastName);
    }
}