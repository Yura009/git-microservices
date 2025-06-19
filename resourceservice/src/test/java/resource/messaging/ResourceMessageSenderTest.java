package resource.messaging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import resource.config.RabbitMQConfig;
import resource.dto.ResourceMessageDto;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ResourceMessageSenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ResourceMessageSender sender;

    @Test
    void shouldSendMessageToQueue() {
        ResourceMessageDto message = new ResourceMessageDto("123");

        sender.sendResourceUploaded(message);

        verify(rabbitTemplate).convertAndSend(RabbitMQConfig.RESOURCE_UPLOADED_QUEUE, message);
    }
}
