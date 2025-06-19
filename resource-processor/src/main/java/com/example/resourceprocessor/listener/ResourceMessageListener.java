package com.example.resourceprocessor.listener;

import com.example.resourceprocessor.config.RabbitMQConfig;
import com.example.resourceprocessor.dto.ResourceMessageDto;
import com.example.resourceprocessor.service.ResourceProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceMessageListener {

    private final ResourceProcessorService processorService;


    @RabbitListener(queues = RabbitMQConfig.RESOURCE_UPLOADED_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    public void handleResourceUpload(ResourceMessageDto message) {
        String resourceId = message.getId();
        log.info("Resource was successfully uploaded id=" + resourceId);
        processorService.processResource(resourceId);
    }

    @RabbitListener(queues = RabbitMQConfig.RESOURCE_DELETED_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    public void handleResourceDelete(ResourceMessageDto message) {
        String resourceId = message.getId();
        log.info("Resource was successfully deleted id=" + resourceId);
        processorService.deleteSongForResource(message.getId());
    }
}
