package br.com.zup.polyana.propostas.viagem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ViagemResponse {

    @JsonProperty("resultado")
    private String resultado;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ViagemResponse(String resultado) {
        this.resultado = resultado;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
