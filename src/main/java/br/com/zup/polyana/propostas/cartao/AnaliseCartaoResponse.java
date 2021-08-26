package br.com.zup.polyana.propostas.cartao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AnaliseCartaoResponse {

    private String id;

    @JsonCreator
    public AnaliseCartaoResponse (@JsonProperty("id") String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
