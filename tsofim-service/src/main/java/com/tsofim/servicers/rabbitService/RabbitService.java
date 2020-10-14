package com.tsofim.servicers.rabbitService;


import com.tsofim.dto.EmailDto;
import com.tsofim.dto.RabbitDto;
import org.springframework.stereotype.Component;

@Component
public interface RabbitService {
    void receivedMessage(RabbitDto incomingMessage);
    void sendToEmailService(EmailDto emailDto);
}
