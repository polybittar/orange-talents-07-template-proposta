package br.com.zup.polyana.propostas.bloqueio;

import br.com.zup.polyana.propostas.cartao.Cartao;
import br.com.zup.polyana.propostas.cartao.CartaoRepository;
import br.com.zup.polyana.propostas.cartao.Client;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@RestController
public class BloqueioCartaoController {

    private Client client;
    private BloqueioCartaoClient bloqueioCartaoClient;
    private CartaoRepository cartaoRepository;

    public BloqueioCartaoController(Client client, BloqueioCartaoClient bloqueioCartaoClient,
                                    CartaoRepository cartaoRepository) {
        this.client = client;
        this.bloqueioCartaoClient = bloqueioCartaoClient;
        this.cartaoRepository = cartaoRepository;
    }

    @RequestMapping(method = RequestMethod.POST, value="/api/cartao/{idCartao}/bloqueio")
    @Transactional
    public ResponseEntity<?> bloqueiaCartao(@PathVariable(required = true,name = "idCartao")
                                                    Long idCartao,
                                            HttpServletRequest request){

        Optional<Cartao> cartaoProcura = cartaoRepository.findById(idCartao);

        if(cartaoProcura.isPresent()) {

            Cartao cartao = cartaoProcura.get();

            cartao.verificaBloqueio(client.buscaIpClient(request),
                    client.buscaUserAgent(request));                    //pega o lugar onde foi feita a request (postman)


            bloqueioCartaoClient.bloqueiaCartao(cartao.getNumero(), Map.of("sistemaResponsavel", "propostas"));
            cartaoRepository.save(cartao);
            return ResponseEntity.ok().build();

        }
        return ResponseEntity.notFound().build();
    }
}
