package com.scheduler.services.rabbitService;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.scheduler.dto.RabbitSenderDto;
import com.scheduler.entity.PlanEntity;
import com.scheduler.enums.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

@Service

public class RabbitServiceImpl implements RabbitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitServiceImpl.class);
    private static final String type = "direct";
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    String exchange;
    @Value("${tsofim.key}")
    String tsofimRoutingkey;
    @Value("${school.key}")
    String schoolRoutingkey;
    @Value("${gymnsat.key}")
    String gymnastRoutingkey;

    @Value(("${tsofim.queue}"))
    String tsofimQueue;
    @Value(("${school.queue}"))
    String schoolQueue;
    @Value(("${gymnsat.queue}"))
    String gymnsatQueue;

    @Value(("${spring.rabbitmq.username}"))
    String rabbitUserName;
    @Value(("${spring.rabbitmq.password}"))
    String rabbitPassword;

    @Override
    public void sendMessageToServer(PlanEntity record) {
        createQuene();
        LocalDateTime dateNow = LocalDateTime.now();
        Services service = record.getService();
        RabbitSenderDto sendMessage = RabbitSenderDto.builder()
                .uuidChild(record.getUuidChild())
                .build();
        switch (service) {
            case TSOFIM:
                rabbitTemplate.convertAndSend(exchange, tsofimRoutingkey, sendMessage);
                break;
            case BEN_GURION:
                rabbitTemplate.convertAndSend(exchange, schoolRoutingkey, sendMessage);
                break;
            case GYMNAST:
                rabbitTemplate.convertAndSend(exchange, gymnastRoutingkey, sendMessage);
                break;
            default:
                LOGGER.info("Not found records" + dateNow.toLocalDate());
                break;
        }
    }

    private void createQuene() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setPassword(rabbitPassword);
        factory.setUsername(rabbitUserName);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(tsofimQueue, false, false, false, null);
            channel.queueDeclare(schoolQueue, false, false, false, null);
            channel.queueDeclare(gymnsatQueue, false, false, false, null);

            channel.exchangeDeclare(exchange, type, true);
            channel.queueBind(tsofimQueue, exchange, tsofimRoutingkey);
            channel.queueBind(schoolQueue, exchange, schoolRoutingkey);
            channel.queueBind(gymnsatQueue, exchange, gymnastRoutingkey);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

