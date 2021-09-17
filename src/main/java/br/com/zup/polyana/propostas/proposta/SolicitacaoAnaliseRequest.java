package br.com.zup.polyana.propostas.proposta;

import br.com.zup.polyana.propostas.proposta.criptografia.CriptografaDocumento;
import br.com.zup.polyana.propostas.validation.validator.CPFOrCNPJ;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//Dados a serem enviados ao client
public class SolicitacaoAnaliseRequest {

    @NotBlank
    @CPFOrCNPJ
    private String documento;
    @NotBlank
    private String nome;
    @NotNull
    private Long idProposta;

    @Deprecated
    public SolicitacaoAnaliseRequest(){

    }

    public SolicitacaoAnaliseRequest(Proposta proposta) {
        this.documento = CriptografaDocumento.decode(proposta.getDocumento());
        this.nome = proposta.getNome();
        this.idProposta = proposta.getId();
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public Long getIdProposta() {
        return idProposta;
    }


}