package br.com.zup.polyana.propostas.proposta;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/proposta")
public class PropostaController {

    private PropostaRepository propostaRepository;

    public PropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody @Valid PropostaRequest propostaRequest, UriComponentsBuilder uriBuilder){

        Proposta proposta = propostaRequest.converter();

        if (proposta.existeProposta(propostaRepository)){
            return ResponseEntity
                    .unprocessableEntity()
                    .build();
        }

        propostaRepository.save(proposta);

        URI uri = uriBuilder.path("/api/proposta/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity
                .created(uri)
                .body(uri);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable Long id) {
        Optional<Proposta> proposta = propostaRepository.findById(id);
        if(proposta.isPresent()) {
            propostaRepository.deleteById(id);
            return ResponseEntity
                    .ok()
                    .build();
        }
        return ResponseEntity
                .notFound()
                .build();
    }
}