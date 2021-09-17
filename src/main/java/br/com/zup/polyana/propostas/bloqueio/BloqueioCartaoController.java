package br.com.zup.polyana.propostas.bloqueio;

import br.com.zup.polyana.propostas.cartao.AnaliseCartaoClient;
import br.com.zup.polyana.propostas.cartao.Cartao;
import br.com.zup.polyana.propostas.cartao.CartaoRepository;
import br.com.zup.polyana.propostas.cartao.Client;
import br.com.zup.polyana.propostas.validation.ApiErrorException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@RestController
public class BloqueioCartaoController {

    private Client client;
    private AnaliseCartaoClient analiseCartaoClient;
    private CartaoRepository cartaoRepository;
    private BloqueioCartao bloqueioCartao;

    public BloqueioCartaoController(Client client, AnaliseCartaoClient analiseCartaoClient,
                                    CartaoRepository cartaoRepository) {
        this.client = client;
        this.analiseCartaoClient = analiseCartaoClient;
        this.cartaoRepository = cartaoRepository;
    }

    @RequestMapping(method = RequestMethod.POST, value="/api/cartao/bloqueio/{idCartao}")
    @Transactional
    public ResponseEntity<?> bloqueiaCartao(@PathVariable(required = true,name = "idCartao")
                                                    Long idCartao,
                                            HttpServletRequest request) {

        Optional<Cartao> possivelCartao = cartaoRepository.findById(idCartao);

        if(possivelCartao.isEmpty()){
            throw new ApiErrorException(HttpStatus.NOT_FOUND, "O cartão não foi encontrado");
        }

        Cartao cartao = possivelCartao.get();

        cartao.verificaBloqueio(client.buscaIpClient(request),
                client.buscaUserAgent(request));                    //pega o lugar onde foi feita a request (postman)

        try{
            analiseCartaoClient.bloqueiaCartao(cartao.getNumero(), Map.of("sistemaResponsavel", "propostas"));
        }catch (FeignException e){
            throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível realizar o bloqueio");
        }

        cartaoRepository.save(cartao);

        return ResponseEntity.ok().build();
    }
}
