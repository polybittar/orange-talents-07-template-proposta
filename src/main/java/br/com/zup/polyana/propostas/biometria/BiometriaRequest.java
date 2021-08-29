package br.com.zup.polyana.propostas.biometria;

import br.com.zup.polyana.propostas.cartao.Cartao;
import br.com.zup.polyana.propostas.cartao.CartaoRepository;
import br.com.zup.polyana.propostas.validation.Base64Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;

public class BiometriaRequest {

    @NotBlank
    @Base64Valid
    private String fingerprint;

    @Deprecated
    public BiometriaRequest() {
    }

    public BiometriaRequest(@NotBlank String fingerprint) {
        this.fingerprint = fingerprint;
    }



    public String getFingerprint() {
        return fingerprint;
    }

    public Biometria converter(Long idCartao, CartaoRepository cartaoRepository) {
        Cartao cartao = cartaoRepository.findById(idCartao)
                .orElseThrow(

                        ()-> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Cartão não encontrado.")

                );

        return new Biometria(this,cartao);
    }

}