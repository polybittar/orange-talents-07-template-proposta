package br.com.zup.polyana.propostas.carteira;

import br.com.zup.polyana.propostas.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Carteira {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoCarteira tipoCarteira;
    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusCarteira status;
    @NotNull
    @ManyToOne
    private Cartao cartao;

    @Deprecated
    public Carteira(){}

    public Carteira(TipoCarteira tipoCarteira, Cartao cartao) {
        this.tipoCarteira = tipoCarteira;
        this.status = StatusCarteira.ASSOCIADA;
        this.cartao = cartao;
    }

    public String getTipoCarteira() {
        return tipoCarteira.toString().replace("_"," ");
    }

    public Long getId() {
        return id;
    }
}