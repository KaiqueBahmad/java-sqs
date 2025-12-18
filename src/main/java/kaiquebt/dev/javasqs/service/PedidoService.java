package kaiquebt.dev.javasqs.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kaiquebt.dev.javasqs.dto.ItemPedidoRequestDTO;
import kaiquebt.dev.javasqs.dto.PedidoDTO;
import kaiquebt.dev.javasqs.dto.PedidoRequestDTO;
import kaiquebt.dev.javasqs.mapper.PedidoMapper;
import kaiquebt.dev.javasqs.model.ItemPedido;
import kaiquebt.dev.javasqs.model.Pedido;
import kaiquebt.dev.javasqs.model.Pedido.StatusPedido;
import kaiquebt.dev.javasqs.model.Produto;
import kaiquebt.dev.javasqs.repository.PedidoRepository;
import kaiquebt.dev.javasqs.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoMapper pedidoMapper;
    private final EstoqueService estoqueService;

    @Transactional
    public PedidoDTO criar(PedidoRequestDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setNomeCliente(dto.getNomeCliente());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PENDENTE);

        List<ItemPedido> itens = dto.getItens().stream()
                .map(itemDto -> criarItemPedido(itemDto, pedido))
                .collect(Collectors.toList());

        pedido.setItens(itens);

        BigDecimal valorTotal = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido.setValorTotal(valorTotal);

        // Decrementar estoque para cada item do pedido
        for (ItemPedido item : itens) {
            estoqueService.removerQuantidade(item.getProduto().getId(), item.getQuantidade());
        }

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return pedidoMapper.toDTO(pedidoSalvo);
    }

    private ItemPedido criarItemPedido(ItemPedidoRequestDTO dto, Pedido pedido) {
        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + dto.getProdutoId()));

        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(dto.getQuantidade());
        item.setPrecoUnitario(produto.getPreco());
        item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(dto.getQuantidade())));

        return item;
    }

    public List<PedidoDTO> listarTodos() {
        return pedidoRepository.findAll()
                .stream()
                .map(pedidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO buscarPorId(UUID id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));
        return pedidoMapper.toDTO(pedido);
    }

    public PedidoDTO atualizar(UUID id, PedidoRequestDTO dto) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));

        pedido.setNomeCliente(dto.getNomeCliente());

        List<ItemPedido> itens = dto.getItens().stream()
                .map(itemDto -> criarItemPedido(itemDto, pedido))
                .collect(Collectors.toList());

        pedido.getItens().clear();
        pedido.getItens().addAll(itens);

        BigDecimal valorTotal = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido.setValorTotal(valorTotal);

        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        return pedidoMapper.toDTO(pedidoAtualizado);
    }

    public void deletar(UUID id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));
        pedidoRepository.delete(pedido);
    }
}
