package kaiquebt.dev.javasqs.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estoque")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estoque {
    @Id
    private UUID produtoId;

    @OneToOne
    @JoinColumn(name = "produto_id", insertable = false, updatable = false)
    private Produto produto;

    private Integer quantidade;
}
