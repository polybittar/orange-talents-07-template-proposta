package br.com.zup.polyana.propostas.viagem;


import br.com.zup.polyana.propostas.cartao.Cartao;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ViagemRequest {

    @NotBlank
    private String destinoViagem;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Future @NotNull
    private LocalDate dataTerminoViagem;

    public ViagemRequest() {

    }

    public ViagemRequest(@NotBlank String destinoViagem,
                           @NotBlank @Future LocalDate dataTerminoViagem) {
        this.destinoViagem = destinoViagem;
        this.dataTerminoViagem = dataTerminoViagem;
    }

    public Viagem converter(Cartao cartao, String ipClient, String userAgent) {
        return new Viagem(cartao, destinoViagem, dataTerminoViagem, ipClient, userAgent);
    }

    public String getDestinoViagem() {
        return destinoViagem;
    }

    public LocalDate getDataTerminoViagem() {
        return dataTerminoViagem;
    }
}