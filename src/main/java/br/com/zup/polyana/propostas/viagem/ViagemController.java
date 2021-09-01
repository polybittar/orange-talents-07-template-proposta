package br.com.zup.polyana.propostas.viagem;


import br.com.zup.polyana.propostas.cartao.AssociaPropostaCartao;
import br.com.zup.polyana.propostas.cartao.Cartao;
import br.com.zup.polyana.propostas.cartao.CartaoRepository;
import br.com.zup.polyana.propostas.cartao.Client;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private ViagemCartaoClient viagemCartaoClient;

    private final Logger logger = LoggerFactory.getLogger(AssociaPropostaCartao.class);

    public ViagemController(Client client, CartaoRepository cartaoRepository,
                            ViagemRepository viagemRepository, ViagemCartaoClient viagemCartaoClient) {
        this.client = client;
        this.cartaoRepository = cartaoRepository;
        this.viagemRepository = viagemRepository;
        this.viagemCartaoClient = viagemCartaoClient;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/cartao/aviso/{numeroCartao}")
    @Transactional
    public ResponseEntity<?> cadastraAvisoViagem(@PathVariable("numeroCartao") String numeroCartao,
                                                 HttpServletRequest request,
                                                 @RequestBody @Valid ViagemRequest viagemRequest){


        Optional<Cartao> possivelCartao = cartaoRepository.findByNumero(numeroCartao);
        String userAgent = client.buscaUserAgent(request);
        String ipClient = client.buscaIpClient(request);

        if(possivelCartao.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Cartao cartao = possivelCartao.get();

        try{
            viagemCartaoClient.avisoViagemCartao(numeroCartao,viagemRequest);
        }catch (FeignException e){
            logger.info("Nao foi possivel cadastrar o aviso de viagem.");
            return ResponseEntity.unprocessableEntity().build();
        }

        Viagem avisoViagem = viagemRequest.converter(cartao, ipClient, userAgent);
        viagemRepository.save(avisoViagem);

        return ResponseEntity.ok().build();
    }
}