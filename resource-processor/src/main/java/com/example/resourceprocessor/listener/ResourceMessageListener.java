package com.example.resourceprocessor.listener;

import com.example.resourceprocessor.config.RabbitMQConfig;
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


    @RabbitListener(queues = RabbitMQConfig.RESOURCE_QUEUE)
    public void handleResourceUpload(String resourceId) {
        log.info("Resource was successfully uploaded id=" + resourceId);
        processorService.processResource(resourceId);
    }
}
