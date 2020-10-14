package com.email.services.rabbitService;

import com.email.dto.EmailDto;
import com.email.dto.EmailVerificationDto;
import com.email.enums.Services;
import com.email.services.emailServices.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitServiceImpl implements RabbitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitServiceImpl.class);


    @Autowired
    EmailService emailService;

    @RabbitListener(queues = "${conf.email.queue}")
    public void receivedConfirmationMessage(EmailVerificationDto incomingMessage) {
        emailService.sendRegistrationEmail(incomingMessage);
    }

    @RabbitListener(queues = "${email.queue}")
    public void receivedMessage(EmailDto incomingMessage) {
        Services service = incomingMessage.getService();
        switch (service) {
            case BEN_GURION:
                emailService.sendSchoolEmail(incomingMessage);
                break;
            case TSOFIM:
                emailService.sendTsofimEmail(incomingMessage);
                break;
            case GYMNAST:
                emailService.sendGymnastEmail(incomingMessage);
                break;
            default:
                LOGGER.error("Service: " + incomingMessage.getService() +
                        " not found");
                break;
        }
    }
}
