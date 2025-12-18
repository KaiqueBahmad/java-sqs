package kaiquebt.dev.javasqs.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kaiquebt.dev.javasqs.dto.EstoqueAjusteDTO;
import kaiquebt.dev.javasqs.dto.EstoqueDTO;
import kaiquebt.dev.javasqs.service.EstoqueService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/estoques")
@RequiredArgsConstructor
public class EstoqueController {

    private final EstoqueService estoqueService;

    @GetMapping
    public ResponseEntity<List<EstoqueDTO>> listarTodos() {
        List<EstoqueDTO> estoques = estoqueService.listarTodos();
        return ResponseEntity.ok(estoques);
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<EstoqueDTO> buscarPorProdutoId(@PathVariable UUID produtoId) {
        EstoqueDTO estoque = estoqueService.buscarPorProdutoId(produtoId);
        return ResponseEntity.ok(estoque);
    }

    @PatchMapping("/produto/{produtoId}/adicionar")
    public ResponseEntity<EstoqueDTO> adicionarQuantidade(
            @PathVariable UUID produtoId,
            @RequestBody EstoqueAjusteDTO dto) {
        EstoqueDTO estoqueAtualizado = estoqueService.adicionarQuantidade(produtoId, dto.getQuantidade());
        return ResponseEntity.ok(estoqueAtualizado);
    }

    @PatchMapping("/produto/{produtoId}/remover")
    public ResponseEntity<EstoqueDTO> removerQuantidade(
            @PathVariable UUID produtoId,
            @RequestBody EstoqueAjusteDTO dto) {
        EstoqueDTO estoqueAtualizado = estoqueService.removerQuantidade(produtoId, dto.getQuantidade());
        return ResponseEntity.ok(estoqueAtualizado);
    }

    @DeleteMapping("/produto/{produtoId}")
    public ResponseEntity<Void> deletar(@PathVariable UUID produtoId) {
        estoqueService.deletar(produtoId);
        return ResponseEntity.noContent().build();
    }
}
