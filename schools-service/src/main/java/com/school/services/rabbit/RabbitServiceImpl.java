package com.school.services.rabbit;


import com.school.dto.EmailDto;
import com.school.dto.RabbitDto;
import com.school.enums.LoggerMessages;
import com.school.services.parser.SchoolCrawlerService;
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
//    @Value("${rabbitmq.exchange}")
//    String exchange;
//
//    @Value("${gymnsat.key}")
//    String gymnastRoutingkey;
//    @Value(("${gymnsat.queue}"))
//    String gymnsatQueue;
//    @Value(("${spring.rabbitmq.username}"))
//    String rabbitUserName;
//    @Value(("${spring.rabbitmq.password}"))
//    String rabbitPassword;
//
//    @Value(("${email.queue}"))
//    String emailQueue;
    @Value(("${email.exchange}"))
    String emailExchange;
    @Value(("${email.key}"))
    String emailRoutingkey;

    @Autowired
    SchoolCrawlerService schoolCrawlerService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    @RabbitListener(queues = "${school.queue}")
    public void receivedMessage(RabbitDto incomingMessage) {
        String uuidChild = incomingMessage.getUuidChild();
        schoolCrawlerService.sendFormToSchool(uuidChild);
        LOGGER.info(LoggerMessages.GET_ATSARAT_BRIUT + LoggerMessages.CHILD + LoggerMessages.WITH_UUID + uuidChild);
        System.out.println("Recieved Message From RabbitMQ: " + incomingMessage.getUuidChild());
    }

    @Override
    public void sendToEmailService(EmailDto emailDto) {
        rabbitTemplate.convertAndSend(emailExchange, emailRoutingkey, emailDto);
    }
}
