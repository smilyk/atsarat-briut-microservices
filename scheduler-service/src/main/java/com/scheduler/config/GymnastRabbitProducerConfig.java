//package com.scheduler.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//
//@Configuration
//public class GymnastRabbitProducerConfig {
////    @Value("${gymnsat.queue}")
////    String queueName;
//    @Value("${rabbitmq.exchange}")
//    String exchange;
////    @Value("${gymnsat.key}")
////    private String routingkey;
//
//
//    @Bean
//    Queue queue() {
//        String queueName = "";
//        return new Queue(queueName, false);
//    }
//
//
//
//
//@Bean
//    DirectExchange exchange() {
//        return new DirectExchange(exchange);
//    }
//

import org.springframework.context.annotation.Bean;

//@Bean
//    Binding binding(Queue queue, DirectExchange exchange) {
//        String routingkey = "";
//        return BindingBuilder.bind(queue).to(exchange).with(routingkey);
//    }
//
//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory, String queue) {
//        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        return rabbitTemplate;
//    }
//}
