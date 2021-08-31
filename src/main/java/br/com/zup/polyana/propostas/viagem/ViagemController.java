package br.com.zup.polyana.propostas.viagem;


import br.com.zup.polyana.propostas.cartao.Cartao;
import br.com.zup.polyana.propostas.cartao.CartaoRepository;
import br.com.zup.polyana.propostas.cartao.Client;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@RestController
public class ViagemController {

    private Client client;
    private CartaoRepository cartaoRepository;
    private ViagemRepository viagemRepository;

    public ViagemController(Client client, CartaoRepository cartaoRepository,
                            ViagemRepository viagemRepository) {
        this.client = client;
        this.cartaoRepository = cartaoRepository;
        this.viagemRepository = viagemRepository;
    }

    @RequestMapping("/api/cartao/{idCartao}/aviso/viagem")
    @Transactional
    public ResponseEntity<?> cadastraAvisoViagem(@PathVariable(required = true,name = "idCartao")
                                                         Long idCartao,
                                                 HttpServletRequest request,
                                                 @RequestBody @Valid ViagemRequest viagemRequest){


        Optional<Cartao> cartaoProcura = cartaoRepository.findById(idCartao);

        if(cartaoProcura.isPresent()) {

            Cartao cartao = cartaoProcura.get();
            Viagem avisoViagem = viagemRequest.converter(cartao,
                    client.buscaIpClient(request),
                    client.buscaUserAgent(request));

            viagemRepository.save(avisoViagem);

            String idGerado = avisoViagem.getIdViagem();

            return ResponseEntity.ok().body(idGerado);
        }
        return ResponseEntity.notFound().build();
    }
}