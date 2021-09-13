package br.com.zup.polyana.propostas.bloqueio;

import br.com.zup.polyana.propostas.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class BloqueioCartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Cartao cartao;
    private LocalDateTime instante = LocalDateTime.now();
    @NotNull
    @Column(nullable = false)
    private String ipCliente;
    @NotNull
    @Column(nullable = false)
    private String userAgent;

    @Deprecated
    public BloqueioCartao() {

    }

    public BloqueioCartao(Cartao cartao,@NotNull String ipCliente,
                    @NotNull String userAgent) {
        this.cartao = cartao;
        this.ipCliente = ipCliente;
        this.userAgent = userAgent;
    }

    public Long getId() {
        return id;
    }
}
