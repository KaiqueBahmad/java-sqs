package kaiquebt.dev.javasqs.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kaiquebt.dev.javasqs.dto.EstoqueDTO;
import kaiquebt.dev.javasqs.mapper.EstoqueMapper;
import kaiquebt.dev.javasqs.model.Estoque;
import kaiquebt.dev.javasqs.repository.EstoqueRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final EstoqueMapper estoqueMapper;

    public List<EstoqueDTO> listarTodos() {
        return estoqueRepository.findAll()
                .stream()
                .map(estoqueMapper::toDTO)
                .collect(Collectors.toList());
    }

    public EstoqueDTO buscarPorProdutoId(UUID produtoId) {
        Estoque estoque = estoqueRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para produto id: " + produtoId));
        return estoqueMapper.toDTO(estoque);
    }

    @Transactional
    public EstoqueDTO adicionarQuantidade(UUID produtoId, Integer quantidade) {
        Estoque estoque = estoqueRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para produto id: " + produtoId));

        estoque.setQuantidade(estoque.getQuantidade() + quantidade);

        if (estoque.getQuantidade() < 0) {
            throw new RuntimeException("Quantidade insuficiente em estoque. Disponível: " + estoque.getQuantidade());
        }

        Estoque estoqueAtualizado = estoqueRepository.save(estoque);
        return estoqueMapper.toDTO(estoqueAtualizado);
    }

    public void deletar(UUID produtoId) {
        Estoque estoque = estoqueRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para produto id: " + produtoId));
        estoqueRepository.delete(estoque);
    }
}
