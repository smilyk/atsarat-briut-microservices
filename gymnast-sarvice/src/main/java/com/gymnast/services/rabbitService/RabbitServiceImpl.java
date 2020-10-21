package com.gymnast.services.rabbitService;

import com.gymnast.dto.EmailDto;
import com.gymnast.dto.RabbitDto;
import com.gymnast.enums.LoggerMessages;
import com.gymnast.services.parseService.GymnastCrawlerService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


@Component
@RefreshScope
public class RabbitServiceImpl implements RabbitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitServiceImpl.class);
    private static final String type = "direct";

    @Value(("${email.exchange}"))
    String emailExchange;
    @Value(("${email.key}"))
    String emailRoutingkey;

    @Autowired
    GymnastCrawlerService gymnastCrawlerService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    @RabbitListener(queues = "${gymnsat.queue}")
    public void receivedMessage(RabbitDto incomingMessage) {
        String uuidChild = incomingMessage.getUuidChild();
        gymnastCrawlerService.sendFormToGymnast(uuidChild);
        LOGGER.info(LoggerMessages.GET_ATSARAT_BRIUT + LoggerMessages.CHILD + LoggerMessages.WITH_UUID + uuidChild);
        System.out.println("Recieved Message From RabbitMQ: " + incomingMessage.getUuidChild());
    }

    @Override
    public void sendToEmailService(EmailDto emailDto) {
        rabbitTemplate.convertAndSend(emailExchange, emailRoutingkey, emailDto);
    }
}
