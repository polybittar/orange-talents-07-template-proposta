package br.com.zup.polyana.propostas.carteira;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CarteiraRequest {
    @NotBlank @Email
    private String email;
    @NotNull
    private TipoCarteira carteira;

    public CarteiraRequest(String email, TipoCarteira carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public String getEmail() {
        return email;
    }

    public TipoCarteira getCarteira() {
        return carteira;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCarteira(TipoCarteira carteira) {
        this.carteira = carteira;
    }
}