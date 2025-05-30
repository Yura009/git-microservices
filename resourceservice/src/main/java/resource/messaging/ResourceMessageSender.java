package resource.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import resource.config.RabbitMQConfig;

@Component
@RequiredArgsConstructor
public class ResourceMessageSender {
    private final RabbitTemplate rabbitTemplate;

    public void sendResourceId(String resourceId) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.RESOURCE_QUEUE, resourceId);
    }
}
