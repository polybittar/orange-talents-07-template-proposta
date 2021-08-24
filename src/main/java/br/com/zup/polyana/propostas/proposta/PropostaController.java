package br.com.zup.polyana.propostas.proposta;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/proposta")
public class PropostaController {

    @PersistenceContext
    private EntityManager manager;

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody @Valid PropostaRequest propostaRequest, UriComponentsBuilder uriBuilder){

        Proposta proposta = propostaRequest.converter();
        manager.persist(proposta);

        URI uri = uriBuilder.path("/api/proposta/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).body(uri);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable Long id) {
        Proposta proposta = manager.find(Proposta.class,id);
        if(proposta!=null) {
            manager.remove(proposta);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}