package kaiquebt.dev.javasqs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kaiquebt.dev.javasqs.service.SqsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsQueueInitializer {

    private final SqsService sqsService;

    @Value("${sqs.queues.pedido_log}")
    private String pedidoLogQueue;
    
    @Value("${sqs.queues.pedido_email}")
    private String pedidoEmailQueue;

    @Value("${sqs.queues.pedido_nota_fiscal}")
    private String pedidoNotaFiscalQueue;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeQueues() {
        log.info("Inicializando filas SQS...");

        createQueue(pedidoLogQueue);
        createQueue(pedidoEmailQueue);
        createQueue(pedidoNotaFiscalQueue);

        log.info("Filas SQS inicializadas com sucesso!");
    }

    private void createQueue(String queueName) {
        try {
            String queueUrl = sqsService.createQueueIfNotExists(queueName, true);
            log.info("Fila criada/verificada: {} -> {}", queueName, queueUrl);
        } catch (Exception e) {
            log.error("Erro ao criar fila: {}", queueName, e);
        }
    }
}
