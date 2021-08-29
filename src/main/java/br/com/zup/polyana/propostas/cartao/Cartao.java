package br.com.zup.polyana.propostas.cartao;

import br.com.zup.polyana.propostas.biometria.Biometria;
import br.com.zup.polyana.propostas.proposta.Proposta;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(nullable = false)
    private String numero;
    @OneToOne(mappedBy = "cartao")
    Proposta proposta;
    @OneToMany(mappedBy = "cartao")
    private Set<Biometria> biometria = new HashSet<Biometria>();


    @Deprecated
    public Cartao() {
    }

    public Cartao(@NotNull String numero, Proposta proposta, Set<Biometria> biometria) {
        this.numero = numero;
        this.proposta = proposta;
        this.biometria = biometria;
    }

    public Cartao(String numero) {
        this.numero = numero;
    }

    public Proposta getProposta() {
        return proposta;
    }


    public Set<Biometria> getBiometria() {
        return biometria;
    }


    public String getNumero() {
        return numero;
    }

}