package br.com.zup.polyana.propostas.proposta;

import br.com.zup.polyana.propostas.proposta.criptografia.CriptografaDocumento;
import br.com.zup.polyana.propostas.validation.validator.CPFOrCNPJ;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class PropostaRequest {

    @NotBlank @CPFOrCNPJ
    private String documento;

    @Email @NotBlank
    private String email;

    @NotBlank
    private String nome;

    @NotBlank
    private String endereco;

    @Positive @NotNull
    private BigDecimal salario;

    @Deprecated
    public PropostaRequest() {
    }

    public PropostaRequest(@NotBlank String documento, @Email @NotBlank String email, @NotBlank String nome,
                        @NotBlank String endereco, @Positive BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public Proposta converter(EstadoProposta estadoProposta) {
        return new Proposta(CriptografaDocumento.encode(documento), email, nome, endereco, salario, estadoProposta);
    }
}
