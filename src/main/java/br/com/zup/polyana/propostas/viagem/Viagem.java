package br.com.zup.polyana.propostas.viagem;


import br.com.zup.polyana.propostas.cartao.Cartao;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private Cartao cartao;
    @NotNull
    @Column(nullable = false)
    private String destino;
    private LocalDateTime instante = LocalDateTime.now();
    @Future
    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dataTerminoViagem;
    @NotNull
    @Column(nullable = false)
    private String ipCliente;
    @NotNull
    @Column(nullable = false)
    private String userAgent;
    @NotNull
    @Column(nullable = false, unique = true)
    private String idViagem;

    @Deprecated
    public Viagem() {

    }

    public Viagem(Cartao cartao, @NotNull String destino,
                       @Future @NotNull LocalDate dataTerminoViagem,
                       @NotNull String ipCliente, @NotNull String userAgent) {
        this.cartao = cartao;
        this.destino = destino;
        this.dataTerminoViagem = dataTerminoViagem;
        this.ipCliente = ipCliente;
        this.userAgent = userAgent;
        this.idViagem = UUID.randomUUID().toString();
    }

    public String getIdViagem() {
        return idViagem;
    }
}