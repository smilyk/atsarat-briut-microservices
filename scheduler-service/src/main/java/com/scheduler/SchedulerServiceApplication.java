package com.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SchedulerServiceApplication {
//	@Value("${rabbitmq.exchange}")
//    String exchange;
	public static void main(String[] args) {
		SpringApplication.run(SchedulerServiceApplication.class, args);
	}
//
//	@Bean
//	DirectExchange exchange() {
//		String name = exchange;
//		return new DirectExchange(name, true, false);
//	}
//
//	    @Bean
//    public MessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//	Binding binding(Queue queue, DirectExchange exchange, String routingkey) {
//		return BindingBuilder.bind(queue).to(exchange).with(routingkey);
//	}
//
//	@Bean
//    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//
//			final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//			rabbitTemplate.setMessageConverter(jsonMessageConverter());
//			return rabbitTemplate;
//    }
}
