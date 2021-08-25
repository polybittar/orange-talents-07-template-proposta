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

    @Enumerated
    @Column(nullable=false)
    private EstadoProposta estadoProposta;

    @Deprecated
    public Proposta() {

    }

    public Proposta(@NotBlank String documento, @Email @NotBlank String email, @NotBlank String nome,
                    @NotBlank String endereco, @Positive BigDecimal salario, EstadoProposta estadoProposta) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
        this.estadoProposta = estadoProposta;
    }

    public Long getId() {
        return id;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public EstadoProposta getEstadoProposta() {
        return estadoProposta;
    }

    public boolean existeProposta(PropostaRepository propostaRepository) {
        return propostaRepository.findByDocumento(documento).isPresent();
    }

    public void atualizaEstado(RestricaoAnalise restricaoAnalise, PropostaRepository repository) {
        this.estadoProposta =
                restricaoAnalise==RestricaoAnalise.COM_RESTRICAO?
                        estadoProposta.NAO_ELEGIVEL:estadoProposta.ELEGIVEL;
        repository.save(this);          //salva o novo estado da proposta após análise

    }

    public SolicitacaoAnaliseResponse executaAnalise(SolicitacaoAnaliseClient encaminhaSolicitacaoAnalise) {
        return encaminhaSolicitacaoAnalise.enviaSolicitacaoAnalise(new SolicitacaoAnaliseRequest(this));
    }
}
