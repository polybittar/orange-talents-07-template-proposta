package br.com.zup.polyana.propostas.viagem;


import br.com.zup.polyana.propostas.cartao.Cartao;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ViagemRequest {

    @NotBlank
    private String destino;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Future @NotNull
    private LocalDate validoAte;

    public ViagemRequest(@NotBlank String destino,
                           @NotBlank @Future LocalDate validoAte) {
        this.destino = destino;
        this.validoAte = validoAte;
    }

    public Viagem converter(Cartao cartao, String ipClient, String userAgent) {
        return new Viagem(cartao, destino, validoAte, ipClient, userAgent);
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }
}