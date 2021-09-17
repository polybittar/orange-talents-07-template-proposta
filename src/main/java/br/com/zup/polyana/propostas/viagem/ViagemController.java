package br.com.zup.polyana.propostas.viagem;


import br.com.zup.polyana.propostas.cartao.*;
import br.com.zup.polyana.propostas.validation.ApiErrorException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@RestController
public class ViagemController {

    private Client client;
    private CartaoRepository cartaoRepository;
    private ViagemRepository viagemRepository;
    private AnaliseCartaoClient analiseCartaoClient;

    private final Logger logger = LoggerFactory.getLogger(ViagemController.class);

    public ViagemController(Client client, CartaoRepository cartaoRepository,
                            ViagemRepository viagemRepository, AnaliseCartaoClient analiseCartaoClient) {
        this.client = client;
        this.cartaoRepository = cartaoRepository;
        this.viagemRepository = viagemRepository;
        this.analiseCartaoClient = analiseCartaoClient;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/cartao/aviso/{idCartao}")
    @Transactional
    public ResponseEntity<?> cadastraAvisoViagem(@PathVariable("idCartao") Long idCartao,
                                                 HttpServletRequest request,
                                                 @RequestBody @Valid ViagemRequest viagemRequest){


        Optional<Cartao> possivelCartao = cartaoRepository.findById(idCartao);
        String userAgent = client.buscaUserAgent(request);
        String ipClient = client.buscaIpClient(request);

        if(possivelCartao.isEmpty()){
            throw new ApiErrorException(HttpStatus.NOT_FOUND, "O cartão não foi encontrado");
        }

        Cartao cartao = possivelCartao.get();

        if(!cartao.naoBloqueado()) {
            throw new ApiErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "O cartão está bloqueado. Não foi possível cadastrar o aviso de viagem");
        }

        try{
            analiseCartaoClient.avisoViagemCartao(cartao.getId().toString(),viagemRequest);
        }catch (FeignException e){
            logger.info("Não foi possível cadastrar o aviso de viagem");
            throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível cadastrar o aviso de viagem");
        }

        Viagem avisoViagem = viagemRequest.converter(cartao, ipClient, userAgent);
        viagemRepository.save(avisoViagem);

        return ResponseEntity.ok().build();
    }
}