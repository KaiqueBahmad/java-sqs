package kaiquebt.dev.javasqs.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {
    private UUID id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
}
