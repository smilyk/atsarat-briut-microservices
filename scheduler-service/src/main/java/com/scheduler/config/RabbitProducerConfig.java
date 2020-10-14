package com.scheduler.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
public class RabbitProducerConfig {
    @Value("${rabbitmq.exchange}")
    String exchange;
    private static final String type = "direct";

    @Value(("${tsofim.queue}"))
    String tsofimQueue;
    @Value(("${school.queue}"))
    String schoolQueue;
    @Value(("${gymnsat.queue}"))
    String gymnsatQueue;
    @Value("${gymnsat.key}")
    String gymnastRoutingkey;
    @Value(("${spring.rabbitmq.username}"))
    String rabbitUserName;
    @Value(("${spring.rabbitmq.password}"))
    String rabbitPassword;
    @Value("${tsofim.key}")
    String tsofimRoutingkey;
    @Value("${school.key}")
    String schoolRoutingkey;


    @Bean
    public void createQuene() {
        com.rabbitmq.client.ConnectionFactory factory = new com.rabbitmq.client.ConnectionFactory();
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
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }

    @Bean
    DirectExchange exchange() {
        String name = exchange;
        return new DirectExchange(name, true, false);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}


