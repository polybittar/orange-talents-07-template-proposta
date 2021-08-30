package br.com.zup.polyana.propostas.cartao;

import br.com.zup.polyana.propostas.biometria.Biometria;
import br.com.zup.polyana.propostas.proposta.Proposta;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
    private Set<Biometria> biometria = new HashSet<>();
    @OneToMany(mappedBy = "cartao",cascade = CascadeType.MERGE)
    private Set<BloqueioCartao> bloqueio = new HashSet<>();
    @Column(nullable=false)
    private EstadoCartao estadoCartao = EstadoCartao.SEM_BLOQUEIO;

    @Deprecated
    public Cartao() {
    }

    public Cartao(@NotNull String numero, Proposta proposta, Set<Biometria> biometria, Set<BloqueioCartao> bloqueio, EstadoCartao estadoCartao) {
        this.numero = numero;
        this.proposta = proposta;
        this.biometria = biometria;
        this.bloqueio = bloqueio;
        this.estadoCartao = estadoCartao;
    }

    public Cartao(String numero) {
        this.numero = numero;
    }

    public Long getId() {
        return id;
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

    public String getEstadoCartao() {
        return estadoCartao.toString().replace("_"," ");
    }

    public void verificaBloqueio(String ip, String userAgent) {

        if(ip!=null && ip.length()>0 &&
                userAgent!=null && userAgent.length()>0 &&
                naoBloqueado()) {
            this.estadoCartao = EstadoCartao.BLOQUEADO;
            this.bloqueio.add(new BloqueioCartao(this, ip, userAgent));

        } else if(!naoBloqueado())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Cartão já se encontra bloqueado.");
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Dados incorretos");
    }

    private boolean naoBloqueado() {
        return this.bloqueio.isEmpty();
    }
}