package br.com.zup.polyana.propostas.carteira;

import br.com.zup.polyana.propostas.cartao.AnaliseCartaoClient;
import br.com.zup.polyana.propostas.cartao.Cartao;
import br.com.zup.polyana.propostas.cartao.CartaoRepository;
import br.com.zup.polyana.propostas.validation.ApiErrorException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
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
import java.util.Objects;
import java.util.Optional;

@RestController
public class CarteiraController {

    private CartaoRepository cartaoRepository;
    private CarteiraRepository carteiraRepository;
    private AnaliseCartaoClient analiseCartaoClient;

    public CarteiraController(CartaoRepository cartaoRepository, CarteiraRepository carteiraRepository,
                              AnaliseCartaoClient analiseCartaoClient) {
        this.cartaoRepository = cartaoRepository;
        this.carteiraRepository =carteiraRepository;
        this.analiseCartaoClient = analiseCartaoClient;
    }

    @RequestMapping("/api/cartao/carteira/{idCartao}")
    @Transactional
    public ResponseEntity associarCarteira(@PathVariable Long idCartao,
                                           @RequestBody @Valid AssociaCarteiraCartao associaCarteiraCartao,
                                           UriComponentsBuilder uriComponentsBuilder){


        Optional<Cartao> possivelCartao = cartaoRepository.findById(idCartao);


        if(possivelCartao.isEmpty()){
            throw new ApiErrorException(HttpStatus.NOT_FOUND, "O cartão não foi encontrado");
        }

        Cartao cartao = possivelCartao.get();

        if(!cartao.naoBloqueado()) {
            throw new ApiErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "O cartão está bloqueado. Não foi possível cadastrar a carteira");
        }

        List<Carteira> carteirasAssociadas = carteiraRepository.findByStatusAndCartaoNumero(StatusCarteira.ASSOCIADA, cartao.getNumero());

        for (Carteira carteiraEncontrada: carteirasAssociadas) {

            if(Objects.equals(carteiraEncontrada.getTipoCarteira(), associaCarteiraCartao.getTipoCarteira().toString())){
                    return ResponseEntity.unprocessableEntity().build();
            }
        }
        Carteira carteira = associaCarteiraCartao.converter(cartao);

        try{
            CarteiraRequest carteiraRequest = new CarteiraRequest(associaCarteiraCartao.getEmail(), associaCarteiraCartao.getTipoCarteira());
            analiseCartaoClient.associaCarteiraDigital(cartao.getNumero(),carteiraRequest);
        } catch (FeignException e){
            throw new ApiErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "Não foi possível cadastrar a carteira");
        }

        carteiraRepository.save(carteira);

        URI uri = uriComponentsBuilder.path("/carteiras/{id}").buildAndExpand(carteira.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}