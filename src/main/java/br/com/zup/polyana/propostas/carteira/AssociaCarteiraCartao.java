package br.com.zup.polyana.propostas.carteira;

import br.com.zup.polyana.propostas.cartao.Cartao;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AssociaCarteiraCartao {
    @NotBlank @Email
    private String email;
    @NotNull
    private TipoCarteira tipoCarteira;

    public AssociaCarteiraCartao(String email, TipoCarteira tipoCarteira) {
        this.email = email;
        this.tipoCarteira = tipoCarteira;
    }

    public TipoCarteira getTipoCarteira() {
        return tipoCarteira;
    }

    public String getEmail() {
        return email;
    }

    public Carteira converter(Cartao cartao){
        return new Carteira(tipoCarteira, cartao);
    }
}