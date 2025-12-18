package kaiquebt.dev.javasqs.mapper;

import org.springframework.stereotype.Component;

import kaiquebt.dev.javasqs.dto.ItemPedidoDTO;
import kaiquebt.dev.javasqs.model.ItemPedido;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ItemPedidoMapper {

    private final ProdutoMapper produtoMapper;

    public ItemPedidoDTO toDTO(ItemPedido itemPedido) {
        if (itemPedido == null) {
            return null;
        }
        return new ItemPedidoDTO(
            itemPedido.getId(),
            produtoMapper.toDTO(itemPedido.getProduto()),
            itemPedido.getQuantidade(),
            itemPedido.getPrecoUnitario(),
            itemPedido.getSubtotal()
        );
    }
}
