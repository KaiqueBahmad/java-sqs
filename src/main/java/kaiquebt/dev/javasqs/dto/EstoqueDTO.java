package kaiquebt.dev.javasqs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueDTO {
    private ProdutoDTO produto;
    private Integer quantidade;
}
