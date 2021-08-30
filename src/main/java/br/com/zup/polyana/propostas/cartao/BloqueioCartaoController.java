package br.com.zup.polyana.propostas.cartao;

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

    private BloqueioCartaoClient bloqueioCartaoClient;
    private CartaoRepository cartaoRepository;

    public BloqueioCartaoController(BloqueioCartaoClient bloqueioCartaoClient, CartaoRepository cartaoRepository) {
        this.bloqueioCartaoClient = bloqueioCartaoClient;
        this.cartaoRepository = cartaoRepository;
    }

    @RequestMapping(method = RequestMethod.POST, value="/api/cartao/{idCartao}/bloqueio")
    @Transactional
    public ResponseEntity<?> bloqueiaCartao(@PathVariable(required = true,name = "idCartao")
                                                    Long idCartao,
                                            HttpServletRequest request){

        Optional<Cartao> cartaoProcura = Optional.ofNullable(cartaoRepository.findById(idCartao)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Cartão não encontrado.")));

        Cartao cartao = cartaoProcura.get();

        String ipClient = request.getHeader("X-FORWARDED-FOR");         //pega o ip de quem mandou a request
        if (ipClient == null) {
            ipClient = request.getRemoteAddr();                           //pega o ip de quem mandou a request
        }
        String userAgent = request.getHeader("USER-AGENT");
        cartao.verificaBloqueio(ipClient, userAgent);                    //pega o lugar onde foi feita a request (postman)

        bloqueioCartaoClient.bloqueiaCartao(cartao.getNumero(), Map.of("sistemaResponsavel", "propostas"));
        cartaoRepository.save(cartao);

        return ResponseEntity.ok().build();
    }
}
