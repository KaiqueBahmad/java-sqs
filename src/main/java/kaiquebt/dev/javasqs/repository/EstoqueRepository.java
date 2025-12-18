package kaiquebt.dev.javasqs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kaiquebt.dev.javasqs.model.Estoque;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, UUID> {
}
