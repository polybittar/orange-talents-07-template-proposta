package br.com.zup.polyana.propostas.cartao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AnaliseCartaoResponse {

    @JsonProperty("id")
    private String id;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AnaliseCartaoResponse (String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Cartao converter() {
        return new Cartao(this.id);
    }
}
