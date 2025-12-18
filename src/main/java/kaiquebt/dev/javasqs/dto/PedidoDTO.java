package kaiquebt.dev.javasqs.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import kaiquebt.dev.javasqs.model.Pedido.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private UUID id;
    private String nomeCliente;
    private LocalDateTime dataPedido;
    private StatusPedido status;
    private List<ItemPedidoDTO> itens;
    private BigDecimal valorTotal;
}
