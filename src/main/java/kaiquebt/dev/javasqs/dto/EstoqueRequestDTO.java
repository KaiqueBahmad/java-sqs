package kaiquebt.dev.javasqs.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueRequestDTO {
    private UUID produtoId;
    private Integer quantidade;
}
