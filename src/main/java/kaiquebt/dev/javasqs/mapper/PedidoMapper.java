package kaiquebt.dev.javasqs.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kaiquebt.dev.javasqs.dto.PedidoDTO;
import kaiquebt.dev.javasqs.model.Pedido;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PedidoMapper {

    private final ItemPedidoMapper itemPedidoMapper;

    public PedidoDTO toDTO(Pedido pedido) {
        if (pedido == null) {
            return null;
        }
        return new PedidoDTO(
            pedido.getId(),
            pedido.getNomeCliente(),
            pedido.getDataPedido(),
            pedido.getStatus(),
            pedido.getItens() != null
                ? pedido.getItens().stream()
                    .map(itemPedidoMapper::toDTO)
                    .collect(Collectors.toList())
                : null,
            pedido.getValorTotal()
        );
    }
}
