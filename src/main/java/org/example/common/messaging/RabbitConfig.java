package org.example.common.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    public static final String ATLAS_EXCHANGE = "atlas.exchange";

    public static final String USER_REGISTERED_QUEUE = "user.registered.queue";

    public static final String USER_REGISTERED_DLQ = "user.registered.dlq";

    public static final String USER_REGISTERED_ROUTING_KEY = "user.registered.routingkey";
    public static final String USER_REGISTERED_DLQ_ROUTING_KEY = "user.registered.dlq.routingkey";


    @Bean
    public Queue userRegisteredQueue() {
        return QueueBuilder
                .durable(USER_REGISTERED_QUEUE)
                .deadLetterExchange(ATLAS_EXCHANGE)
                .deadLetterRoutingKey(USER_REGISTERED_DLQ_ROUTING_KEY)
                .build();
    }


    @Bean
    public Queue userRegisteredDlq() {
        return new Queue(USER_REGISTERED_DLQ, true);
    }


    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(ATLAS_EXCHANGE);
    }



    @Bean
    public Binding userRegisteredBinding(Queue userRegisteredQueue, DirectExchange exchange) {
        return BindingBuilder
                .bind(userRegisteredQueue)
                .to(exchange)
                .with(USER_REGISTERED_ROUTING_KEY);
    }


    @Bean
    public Binding userRegisteredDlqBinding(Queue userRegisteredDlq, DirectExchange exchange) {
        return BindingBuilder
                .bind(userRegisteredDlq)
                .to(exchange)
                .with(USER_REGISTERED_DLQ_ROUTING_KEY);
    }




    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {

        return new Jackson2JsonMessageConverter(objectMapper);

    }

}