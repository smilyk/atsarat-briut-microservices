package com.school.services.rabbit;


import com.school.dto.EmailDto;
import com.school.dto.RabbitDto;
import org.springframework.stereotype.Component;

@Component
public interface RabbitService {
    void receivedMessage(RabbitDto incomingMessage);
    void sendToEmailService(EmailDto emailDto);
}
