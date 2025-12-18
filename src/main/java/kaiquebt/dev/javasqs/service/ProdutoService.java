package kaiquebt.dev.javasqs.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kaiquebt.dev.javasqs.dto.ProdutoDTO;
import kaiquebt.dev.javasqs.dto.ProdutoRequestDTO;
import kaiquebt.dev.javasqs.mapper.ProdutoMapper;
import kaiquebt.dev.javasqs.model.Estoque;
import kaiquebt.dev.javasqs.model.Produto;
import kaiquebt.dev.javasqs.repository.EstoqueRepository;
import kaiquebt.dev.javasqs.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;
    private final ProdutoMapper produtoMapper;

    @Transactional
    public ProdutoDTO criar(ProdutoRequestDTO dto) {
        Produto produto = produtoMapper.toEntity(dto);
        Produto produtoSalvo = produtoRepository.save(produto);

        // Criar estoque zerado automaticamente
        Estoque estoque = new Estoque();
        estoque.setProdutoId(produtoSalvo.getId());
        estoque.setQuantidade(0);
        estoqueRepository.save(estoque);

        return produtoMapper.toDTO(produtoSalvo);
    }

    public List<ProdutoDTO> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(produtoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProdutoDTO buscarPorId(UUID id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + id));
        return produtoMapper.toDTO(produto);
    }

    public ProdutoDTO atualizar(UUID id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + id));
        produtoMapper.updateEntity(dto, produto);
        Produto produtoAtualizado = produtoRepository.save(produto);
        return produtoMapper.toDTO(produtoAtualizado);
    }

    public void deletar(UUID id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + id));
        produtoRepository.delete(produto);
    }
}
