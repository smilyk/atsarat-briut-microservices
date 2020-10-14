package com.email.services.emailServices;

import com.email.dto.EmailDto;
import com.email.dto.EmailVerificationDto;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    String sendRegistrationEmail(EmailVerificationDto mail);

    String sendGymnastEmail(EmailDto mail);

    String sendSchoolEmail(EmailDto mail);

    String sendTsofimEmail(EmailDto mail);

    void emailError(String email, String service, String userFirstName, String userLastName);
}

