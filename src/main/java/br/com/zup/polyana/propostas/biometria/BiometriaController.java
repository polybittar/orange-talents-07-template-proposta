package br.com.zup.polyana.propostas.biometria;

import br.com.zup.polyana.propostas.cartao.CartaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
public class BiometriaController {

    private BiometriaRepository biometriaRepository;
    private CartaoRepository cartaoRepository;

    public BiometriaController(BiometriaRepository biometriaRepository, CartaoRepository cartaoRepository) {
        this.biometriaRepository = biometriaRepository;
        this.cartaoRepository = cartaoRepository;
    }

    @RequestMapping(method = RequestMethod.POST, value="/api/cartao/{id}/biometria")
    @Transactional
    public ResponseEntity<?> cadastraBiometria(@RequestBody @Valid BiometriaRequest biometriaRequest,
                                               @PathVariable(name = "id") Long id,
                                               UriComponentsBuilder uriBuilder){

        Biometria biometria = biometriaRequest.converter(id, cartaoRepository);
        biometriaRepository.save(biometria);

        URI uri = uriBuilder.path("/biometria/{id}").buildAndExpand(biometria.getId()).toUri();
        return ResponseEntity
                .created(uri)
                .body(uri);
    }
}