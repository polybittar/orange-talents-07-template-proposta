package br.com.zup.polyana.propostas.proposta;


import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
public class PropostaController {

    private PropostaRepository propostaRepository;
    private SolicitacaoAnaliseClient encaminhaSolicitacaoAnalise;

    public PropostaController(PropostaRepository propostaRepository, SolicitacaoAnaliseClient encaminhaSolicitacaoAnalise) {
        this.propostaRepository = propostaRepository;
        this.encaminhaSolicitacaoAnalise = encaminhaSolicitacaoAnalise;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/proposta")
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody @Valid PropostaRequest propostaRequest, UriComponentsBuilder uriBuilder){

        //começa como não elegível antes da verificação
        Proposta proposta = propostaRequest.converter(EstadoProposta.NÃO_ELEGÍVEL);

        //confere se já existe uma proposta com o documento e volta 422 caso for verdade
        if (proposta.existeProposta(propostaRepository)){
            return ResponseEntity
                    .unprocessableEntity()
                    .build();
        }

        //salva antes da análise
        propostaRepository.save(proposta);
        URI uri = uriBuilder.path("/api/proposta/{idProposta}").buildAndExpand(proposta.getIdProposta()).toUri();

        try {
            //tenta atualizar o estado se não houver restrição após análise
            proposta.atualizaEstado(proposta.executaAnalise(encaminhaSolicitacaoAnalise)
                            .getResultadoSolicitacao(),
                    propostaRepository);

        }catch (FeignException.UnprocessableEntity e) {
            //status 422 se houver restrição
            proposta.atualizaEstado(
                    RestricaoAnalise.COM_RESTRICAO,
                    propostaRepository);

            return ResponseEntity
                    .status(e.status())
                    .body(uri);
        }

        //retorna 201 com uri caso estiver elegível
        return ResponseEntity
                .created(uri)
                .body(uri);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/proposta/{id}")
    @Transactional
    public ResponseEntity<?> consultar(@PathVariable String id) {

        Optional<Proposta> propostaObject = propostaRepository.findByIdProposta(id);
        if(propostaObject.isPresent()) {
            Proposta proposta = propostaObject.get();
            return ResponseEntity.ok(new PropostaResponse(proposta));
        }
        return ResponseEntity
                .notFound()
                .build();
    }



    @RequestMapping(method = RequestMethod.DELETE, value = "/api/proposta/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable String id) {
        Optional<Proposta> proposta = propostaRepository.findByIdProposta(id);
        if(proposta.isPresent()) {
            propostaRepository.deleteByIdProposta(id);
            return ResponseEntity
                    .ok()
                    .build();
        }
        return ResponseEntity
                .notFound()
                .build();
    }
}