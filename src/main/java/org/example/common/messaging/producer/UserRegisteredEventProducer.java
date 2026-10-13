package org.example.common.messaging.producer;


import org.example.common.messaging.RabbitConfig;
import org.example.common.messaging.event.UserRegisteredEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
public class UserRegisteredEventProducer {


    private final RabbitTemplate rabbitTemplate;


    public UserRegisteredEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void publishUserRegistered(UserRegisteredEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.ATLAS_EXCHANGE,
                RabbitConfig.USER_REGISTERED_ROUTING_KEY,
                event
        );

    }

}
