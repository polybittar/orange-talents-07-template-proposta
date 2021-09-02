package br.com.zup.polyana.propostas.carteira;

public class CarteiraResponse {
    private String resultado;
    private String id;

    public CarteiraResponse(String resultado, String id) {
        this.resultado = resultado;
        this.id = id;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}