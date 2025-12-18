package kaiquebt.dev.javasqs.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kaiquebt.dev.javasqs.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoQueueProducer {
    
    private final SqsService sqsService;
    
    @Value("${sqs.queues.pedido_log}")
    private String pedidoLogQueue;
    
    @Value("${sqs.queues.pedido_email}")
    private String pedidoEmailQueue;

    @Value("${sqs.queues.pedido_nota_fiscal}")
    private String pedidoNotaFiscalQueue;

    @Data
    @NoArgsConstructor
    public static class CommonPedidoMessage {
        public CommonPedidoMessage(UUID pedidoId) {
            if (pedidoId == null) {
                throw new IllegalArgumentException("Id do pedido não pode ser nulo na construção de uma mensagem");
            }
            this.pedidoId = pedidoId;
        }

        private UUID pedidoId;
    }

    public void producePedidoLog(Pedido pedido) {
        sqsService.sendObjectMessage(pedidoLogQueue, new CommonPedidoMessage(pedido.getId()));
    }

    public void producePedidoEmail(Pedido pedido) {
        sqsService.sendObjectMessage(pedidoEmailQueue, new CommonPedidoMessage(pedido.getId()));
    }

    public void producePedidoNotaFiscal(Pedido pedido) {
        sqsService.sendObjectMessage(pedidoNotaFiscalQueue, new CommonPedidoMessage(pedido.getId()));
    }

}
