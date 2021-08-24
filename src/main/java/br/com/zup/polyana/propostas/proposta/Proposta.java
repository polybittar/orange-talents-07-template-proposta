package br.com.zup.polyana.propostas.proposta;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String documento;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(nullable = false)
    private String endereco;

    @Positive
    @Column(nullable = false)
    @NotNull
    private BigDecimal salario;

    @Deprecated
    public Proposta() {

    }

    public Proposta(@NotBlank String documento, @Email @NotBlank String email, @NotBlank String nome,
                    @NotBlank String endereco, @Positive BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public boolean existeProposta(PropostaRepository propostaRepository) {
        return propostaRepository.findByDocumento(documento).isPresent();
    }

    public Long getId() {
        return id;
    }
}
