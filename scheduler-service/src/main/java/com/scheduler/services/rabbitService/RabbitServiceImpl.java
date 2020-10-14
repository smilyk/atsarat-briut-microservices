package com.scheduler.services.rabbitService;

import com.scheduler.dto.RabbitSenderDto;
import com.scheduler.entity.PlanEntity;
import com.scheduler.enums.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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


    @Override
    public void sendMessageToServer(PlanEntity record) {
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
}

