package kaiquebt.dev.javasqs.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;

@Service
@RequiredArgsConstructor
public class SqsService {

    private final SqsTemplate sqsTemplate;
    private final SqsAsyncClient sqsAsyncClient;
    private final ObjectMapper objectMapper;

    public void sendObjectMessage(String queueName, Object payload) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(payload);
            this.sendMessage(queueName, jsonMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter objeto para JSON", e);
        }
    }

    public void sendMessage(String queueName, String messageBody) {
        sqsTemplate.send(queueName, messageBody);
    }

    public void sendMessageToUrl(String queueUrl, String messageBody) {
        sqsTemplate.send(to -> to.queue(queueUrl).payload(messageBody));
    }

    public String getQueueUrl(String queueName) {
        GetQueueUrlRequest request = GetQueueUrlRequest.builder()
                .queueName(queueName)
                .build();
        return sqsAsyncClient.getQueueUrl(request).join().queueUrl();
    }

    public String createQueue(String queueName, boolean isFifo) {
        Map<QueueAttributeName, String> attributes = new HashMap<>();

        if (isFifo) {
            attributes.put(QueueAttributeName.FIFO_QUEUE, "true");
            attributes.put(QueueAttributeName.CONTENT_BASED_DEDUPLICATION, "true");

            if (!queueName.endsWith(".fifo")) {
                queueName = queueName + ".fifo";
            }
        }

        CreateQueueRequest request = CreateQueueRequest.builder()
                .queueName(queueName)
                .attributes(attributes)
                .build();

        return sqsAsyncClient.createQueue(request).join().queueUrl();
    }

    public String createQueueIfNotExists(String queueName, boolean isFifo) {
        try {
            return getQueueUrl(queueName);
        } catch (Exception e) {
            if (e instanceof QueueDoesNotExistException || e.getCause() instanceof QueueDoesNotExistException) {
                System.out.println("Fila não existe, criando...");
                return createQueue(queueName, isFifo);
            }
            
            return null;
        }
    }
}
