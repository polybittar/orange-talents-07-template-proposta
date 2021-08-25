package br.com.zup.polyana.propostas.proposta;

import br.com.zup.polyana.propostas.validation.CPFOrCNPJ;

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
    private String idProposta;

    public SolicitacaoAnaliseRequest(String nome, String documento, String idProposta) {
        this.nome = nome;
        this.documento = documento;
        this.idProposta = idProposta;
    }

    public SolicitacaoAnaliseRequest(Proposta proposta) {
        this.documento = proposta.getDocumento();
        this.nome = proposta.getNome();
        this.idProposta = proposta.getId().toString();
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getIdProposta() {
        return idProposta;
    }


}