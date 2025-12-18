package kaiquebt.dev.javasqs.consumer;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.sqs.annotation.SqsListener;
import kaiquebt.dev.javasqs.model.Pedido;
import kaiquebt.dev.javasqs.repository.PedidoRepository;
import kaiquebt.dev.javasqs.service.PedidoQueueProducer;
import kaiquebt.dev.javasqs.service.PedidoQueueProducer.CommonPedidoMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoConsumer {
    private final PedidoQueueProducer pedidoQueueProducer;
    private final PedidoRepository pedidoRepository;
    private final ObjectMapper objectMapper;

    @Value("${sqs.queues.pedido_log}")
    private String pedidoLogQueue;

    @Value("${sqs.queues.pedido_email}")
    private String pedidoEmailQueue;

    @Value("${sqs.queues.pedido_nota_fiscal}")
    private String pedidoNotaFiscalQueue;

    @SqsListener(queueNames = "${sqs.queues.pedido_log}")
    public void consumePedidoLog(String message) {
        try {
            log.info("Recebendo mensagem da fila {}: {}", pedidoLogQueue, message);

            CommonPedidoMessage pedidoMessage = objectMapper.readValue(message, CommonPedidoMessage.class);
            UUID pedidoId = pedidoMessage.getPedidoId();

            Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + pedidoId));

            log.info("LOG - Pedido processado: ID={}, Cliente={}, Status={}, Valor={}",
                pedido.getId(), pedido.getNomeCliente(), pedido.getStatus(), pedido.getValorTotal());

        } catch (Exception e) {
            log.error("Erro ao processar mensagem da fila pedido_log", e);
        }
    }


    @SqsListener(queueNames = "${sqs.queues.pedido_nota_fiscal}")
    public void consumePedidoNotaFiscal(String message) {
        try {
            log.info("Recebendo mensagem da fila {}: {}", pedidoNotaFiscalQueue, message);

            CommonPedidoMessage pedidoMessage = objectMapper.readValue(message, CommonPedidoMessage.class);
            UUID pedidoId = pedidoMessage.getPedidoId();

            Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + pedidoId));

            log.info("NOTA FISCAL - Gerando nota fiscal para pedido ID: {}", pedido.getId());
            log.info("NOTA FISCAL - Cliente: {}", pedido.getNomeCliente());
            log.info("NOTA FISCAL - Data: {}", pedido.getDataPedido());
            log.info("NOTA FISCAL - Valor Total: {}", pedido.getValorTotal());
            log.info("NOTA FISCAL - Número de itens: {}", pedido.getItens().size());

            pedidoQueueProducer.producePedidoEmail(pedido);
        } catch (Exception e) {
            log.error("Erro ao processar mensagem da fila pedido_nota_fiscal", e);
        }
    }

    @SqsListener(queueNames = "${sqs.queues.pedido_email}")
    public void consumePedidoEmail(String message) {
        try {
            log.info("Recebendo mensagem da fila {}: {}", pedidoEmailQueue, message);

            CommonPedidoMessage pedidoMessage = objectMapper.readValue(message, CommonPedidoMessage.class);
            UUID pedidoId = pedidoMessage.getPedidoId();

            Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + pedidoId));

            log.info("EMAIL - Enviando email para cliente: {} sobre pedido ID: {}",
                pedido.getNomeCliente(), pedido.getId());
            log.info("EMAIL - Assunto: Confirmação do Pedido #{}", pedido.getId());
            log.info("EMAIL - Valor Total: {}", pedido.getValorTotal());

        } catch (Exception e) {
            log.error("Erro ao processar mensagem da fila pedido_email", e);
        }
    }
}
