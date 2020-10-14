package com.gymnast.services.rabbitService;

import com.gymnast.dto.EmailDto;
import com.gymnast.dto.RabbitDto;
import org.springframework.stereotype.Component;

@Component
public interface RabbitService {
    void receivedMessage(RabbitDto incomingMessage);
    void sendToEmailService(EmailDto emailDto);
}
