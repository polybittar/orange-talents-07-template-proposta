package br.com.zup.polyana.propostas.cartao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartaoRepository extends JpaRepository<Cartao,Long> {
    Optional<Cartao> findById(Long id);
}
