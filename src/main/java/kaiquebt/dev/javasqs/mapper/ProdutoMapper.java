package kaiquebt.dev.javasqs.mapper;

import org.springframework.stereotype.Component;

import kaiquebt.dev.javasqs.dto.ProdutoDTO;
import kaiquebt.dev.javasqs.dto.ProdutoRequestDTO;
import kaiquebt.dev.javasqs.model.Produto;

@Component
public class ProdutoMapper {

    public ProdutoDTO toDTO(Produto produto) {
        if (produto == null) {
            return null;
        }
        return new ProdutoDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco()
        );
    }

    public Produto toEntity(ProdutoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        return produto;
    }

    public void updateEntity(ProdutoRequestDTO dto, Produto produto) {
        if (dto == null || produto == null) {
            return;
        }
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
    }
}
