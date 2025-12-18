package kaiquebt.dev.javasqs.mapper;

import org.springframework.stereotype.Component;

import kaiquebt.dev.javasqs.dto.EstoqueDTO;
import kaiquebt.dev.javasqs.dto.EstoqueRequestDTO;
import kaiquebt.dev.javasqs.model.Estoque;
import kaiquebt.dev.javasqs.model.Produto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EstoqueMapper {

    private final ProdutoMapper produtoMapper;

    public EstoqueDTO toDTO(Estoque estoque) {
        if (estoque == null) {
            return null;
        }
        return new EstoqueDTO(
            produtoMapper.toDTO(estoque.getProduto()),
            estoque.getQuantidade()
        );
    }

    public Estoque toEntity(EstoqueRequestDTO dto, Produto produto) {
        if (dto == null) {
            return null;
        }
        Estoque estoque = new Estoque();
        estoque.setQuantidade(dto.getQuantidade());
        return estoque;
    }

    public void updateEntity(EstoqueRequestDTO dto, Estoque estoque, Produto produto) {
        if (dto == null || estoque == null) {
            return;
        }
        estoque.setQuantidade(dto.getQuantidade());
    }
}
