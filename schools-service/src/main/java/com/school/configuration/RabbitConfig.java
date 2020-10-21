package com.school.configuration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
@RefreshScope
public class RabbitConfig {
    private static final String type = "direct";

    @Value("${rabbitmq.exchange}")
    String exchange;
    @Value("${school.key}")
    String schoolRoutingkey;
    @Value(("${school.queue}"))
    String schoolQueue;
    @Value(("${spring.rabbitmq.username}"))
    String rabbitUserName;
    @Value(("${spring.rabbitmq.password}"))
    String rabbitPassword;

    @Value(("${email.queue}"))
    String emailQueue;
    @Value(("${email.exchange}"))
    String emailExchange;
    @Value(("${email.key}"))
    String emailRoutingkey;

    @Bean
    public void createSchoolQueue() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setPassword(rabbitPassword);
        factory.setUsername(rabbitUserName);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(schoolQueue, false, false, false, null);
            channel.exchangeDeclare(exchange, type, true);
            channel.queueBind(schoolQueue, exchange, schoolRoutingkey);
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
}

    @Bean
    public void createEmailQueue() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setPassword(rabbitPassword);
        factory.setUsername(rabbitUserName);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(emailQueue, false, false, false, null);
            channel.exchangeDeclare(emailExchange, type, true);
            channel.queueBind(emailQueue, emailExchange, emailRoutingkey);
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
