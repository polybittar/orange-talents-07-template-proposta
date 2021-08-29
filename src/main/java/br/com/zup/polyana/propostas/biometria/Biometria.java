package br.com.zup.polyana.propostas.biometria;

import br.com.zup.polyana.propostas.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime data = LocalDateTime.now();
    @NotNull
    @Column(nullable = false)
    private String fingerprint;
    @ManyToOne
    private Cartao cartao;

    @Deprecated
    public Biometria() {

    }

    public Biometria(LocalDateTime data, @NotNull String fingerprint, Cartao cartao) {
        this.data = data;
        this.fingerprint = fingerprint;
        this.cartao = cartao;
    }

    public Biometria(BiometriaRequest biometriaRequest,Cartao cartao) {
        this.cartao = cartao;
        this.fingerprint = biometriaRequest.getFingerprint();
    }

    public Long getId() {
        return id;
    }
}