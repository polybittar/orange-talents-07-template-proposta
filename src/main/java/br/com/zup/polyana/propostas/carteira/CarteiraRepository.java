package br.com.zup.polyana.propostas.carteira;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarteiraRepository extends JpaRepository<Carteira, Long> {
    List<Carteira> findByStatusAndCartaoNumero(StatusCarteira status, String numero);
}