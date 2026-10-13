package org.example.common.messaging;

import com.rabbitmq.client.GetResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
public class RecoveryDlqMessageService {


    private final RabbitTemplate rabbitTemplate;


    public RecoveryDlqMessageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void recoverMessages() {

        while (true) {

            Boolean recovered = rabbitTemplate.execute(channel -> {

                GetResponse response = channel.basicGet(
                        RabbitConfig.USER_REGISTERED_DLQ,
                        false
                );


                if (response == null) {
                    return null;
                }


                try {

                    channel.confirmSelect();

                    channel.basicPublish(
                            RabbitConfig.ATLAS_EXCHANGE,
                            RabbitConfig.USER_REGISTERED_ROUTING_KEY,
                            response.getProps(),
                            response.getBody()
                    );


                    boolean confirmed = channel.waitForConfirms(5000);

                    if (!confirmed) {
                        return false;
                    }


                    channel.basicAck(
                            response.getEnvelope().getDeliveryTag(),
                            false
                    );

                    return true;

                } catch (Exception e) {

                    return false;

                }
            });


            if (recovered == null) {
                break;
            }

            if (!recovered) {
                break;
            }

        }

    }

}