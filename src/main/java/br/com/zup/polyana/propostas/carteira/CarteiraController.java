package br.com.zup.polyana.propostas.carteira;

import br.com.zup.polyana.propostas.cartao.Cartao;
import br.com.zup.polyana.propostas.cartao.CartaoRepository;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class CarteiraController {

    private CartaoRepository cartaoRepository;
    private CarteiraRepository carteiraRepository;
    private CarteiraCartaoClient carteiraCartaoClient;

    public CarteiraController(CartaoRepository cartaoRepository, CarteiraRepository carteiraRepository,
                              CarteiraCartaoClient carteiraCartaoClient) {
        this.cartaoRepository = cartaoRepository;
        this.carteiraRepository =carteiraRepository;
        this.carteiraCartaoClient = carteiraCartaoClient;
    }

    @RequestMapping("/api/cartao/{numeroCartao}/carteira")
    @Transactional
    public ResponseEntity associarCarteira(@PathVariable String numeroCartao,
                                           @RequestBody @Valid AssociaCarteiraCartao associaCarteiraCartao,
                                           UriComponentsBuilder uriComponentsBuilder){

        Optional<Cartao> possivelCartao = cartaoRepository.findByNumero(numeroCartao);

        if(possivelCartao.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Cartao cartao = possivelCartao.get();
        List<Carteira> carteirasAssociadas = carteiraRepository.findByStatusAndCartaoNumero(StatusCarteira.ASSOCIADA, numeroCartao);

        for (Carteira carteiraEncontrada: carteirasAssociadas) {

            if(carteiraEncontrada.getTipoCarteira() == associaCarteiraCartao.getTipoCarteira()){
                return ResponseEntity.unprocessableEntity().build();
            }
        }
        Carteira carteira = associaCarteiraCartao.converter(cartao);

        try{
            CarteiraRequest carteiraRequest = new CarteiraRequest(associaCarteiraCartao.getEmail(), associaCarteiraCartao.getTipoCarteira());
            carteiraCartaoClient.associaCarteiraDigital(numeroCartao,carteiraRequest);
        } catch (FeignException e){
            return ResponseEntity.unprocessableEntity().build();
        }

        carteiraRepository.save(carteira);

        URI uri = uriComponentsBuilder.path("/carteiras/{id}").buildAndExpand(carteira.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}