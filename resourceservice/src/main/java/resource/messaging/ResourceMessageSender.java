package resource.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import resource.config.RabbitMQConfig;
import resource.dto.ResourceMessageDto;
import resource.exception.FailedToSendResourceException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceMessageSender {
    private final RabbitTemplate rabbitTemplate;

    @Retryable(
            retryFor = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void sendResourceUploaded(ResourceMessageDto message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.RESOURCE_UPLOADED_QUEUE, message);
    }

    public void sendResourceDeleted(ResourceMessageDto message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.RESOURCE_DELETED_QUEUE, message);
    }


    @Recover
    public void recover(Exception ex, ResourceMessageDto message) {
        log.error("Failed to send message with {} ", message.getId(), ex);
        throw new FailedToSendResourceException("Resource was not sent");
    }
}
