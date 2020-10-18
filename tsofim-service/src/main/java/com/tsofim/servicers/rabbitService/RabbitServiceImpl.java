package com.tsofim.servicers.rabbitService;


import com.tsofim.dto.EmailDto;
import com.tsofim.dto.RabbitDto;
import com.tsofim.enums.LoggerMessages;
import com.tsofim.servicers.parser.TsofimCrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitServiceImpl implements RabbitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitServiceImpl.class);
    private static final String type = "direct";

    @Value(("${email.exchange}"))
    String emailExchange;
    @Value(("${email.key}"))
    String emailRoutingkey;

    @Autowired
    TsofimCrawlerService gymnastCrawlerService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    @RabbitListener(queues = "${tsofim.queue}")
    public void receivedMessage(RabbitDto incomingMessage) {
        String uuidChild = incomingMessage.getUuidChild();
        gymnastCrawlerService.sendFormToTsofim(uuidChild);
        LOGGER.info(LoggerMessages.GET_ATSARAT_BRIUT + LoggerMessages.CHILD + LoggerMessages.WITH_UUID + uuidChild);
        System.out.println("Recieved Message From RabbitMQ: " + incomingMessage.getUuidChild());
    }

    @Override
    public void sendToEmailService(EmailDto emailDto) {
        rabbitTemplate.convertAndSend(emailExchange, emailRoutingkey, emailDto);
    }
}