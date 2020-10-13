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
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

@Service

public class RabbitServiceImpl implements RabbitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitServiceImpl.class);
    @Autowired
    private AmqpTemplate rabbitTemplate;


//    @Value("${rabbitmq.exchange}")
    String exchange = "atBriut";
    @Value("${tsofim.key}")
    String tsofimRoutingkey;
    //    @Value("${school.rabbitmq.exchange}")
//    String schoolExchange;
    @Value("${school.key}")
    String schoolRoutingkey;
    //    @Value("${gymnast.rabbitmq.exchange}")
//    String gymnastExchange;
    @Value("${gymnsat.key}")
    String gymnastRoutingkey;

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Override

    public void sendMessageToServer(PlanEntity record){
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
        factory.setPassword("password");
        factory.setUsername("user");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare("TSOFIM", false, false, false, null);
            channel.queueDeclare("SCHOOL", false, false, false, null);
            channel.queueDeclare("GYMNAST", false, false, false, null);
            String tsofim = "TSOFIM";
            channel.exchangeDeclare(exchange, "direct", true);
            channel.queueBind(tsofim, exchange, "ts");
            channel.queueBind("SCHOOL", exchange, "sc");
            channel.queueBind("GYMNAST", exchange, "gym");
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

