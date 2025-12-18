package kaiquebt.dev.javasqs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kaiquebt.dev.javasqs.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
}
