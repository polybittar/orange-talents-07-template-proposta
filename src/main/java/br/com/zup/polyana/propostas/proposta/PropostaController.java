package br.com.zup.polyana.propostas.proposta;


import br.com.zup.polyana.propostas.proposta.criptografia.CriptografaDocumento;
import br.com.zup.polyana.propostas.validation.ApiErrorException;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class PropostaController {

    private PropostaRepository propostaRepository;
    private SolicitacaoAnaliseClient encaminhaSolicitacaoAnalise;
    private final Tracer tracer;

    public PropostaController(PropostaRepository propostaRepository, SolicitacaoAnaliseClient encaminhaSolicitacaoAnalise,
                              Tracer tracer) {
        this.propostaRepository = propostaRepository;
        this.encaminhaSolicitacaoAnalise = encaminhaSolicitacaoAnalise;
        this.tracer = tracer;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/proposta")
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody @Valid PropostaRequest propostaRequest,
                                       UriComponentsBuilder uriBuilder){
        Span activeSpan = tracer.activeSpan();
        activeSpan.setTag("user.email",propostaRequest.getEmail());
        //activeSpan.setBaggageItem("user.email", propostaRequest.getEmail());
        //activeSpan.log("Meu log");

        Optional<Proposta> optionalProposta = propostaRepository.findByDocumento(propostaRequest.getDocumento());

        if(documentoExiste(propostaRequest.getDocumento())){
            throw new ApiErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "Permitida somente uma proposta por pessoa." );
        }

        //começa como não elegível antes da verificação
        Proposta proposta = propostaRequest.converter(EstadoProposta.NÃO_ELEGÍVEL);


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

            throw new ApiErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "Proposta restrita: " + uri);

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

    private Boolean documentoExiste(String documento){
        List<Proposta> propostas = propostaRepository.findAll();
        AtomicReference<Boolean> existe = new AtomicReference<>(false);
        propostas.forEach(proposta->{
            String documentoLimpo = CriptografaDocumento.decode(proposta.getDocumento());
            if(documentoLimpo.equals(documento)){
                existe.set(true);
            }else{
                existe.set(false);
            }
        });

        return existe.get();
    }
}