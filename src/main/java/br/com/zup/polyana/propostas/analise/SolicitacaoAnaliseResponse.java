package br.com.zup.polyana.propostas.analise;

import br.com.zup.polyana.propostas.proposta.RestricaoAnalise;

import javax.persistence.Enumerated;

public class SolicitacaoAnaliseResponse {

    private String documento;
    private String nome;

    @Enumerated
    private RestricaoAnalise resultadoSolicitacao;

    private Long idProposta;


    public SolicitacaoAnaliseResponse(String documento, String nome, RestricaoAnalise resultadoSolicitacao,
                                       Long idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
        this.idProposta = idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public RestricaoAnalise getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }

    public Long getIdProposta() {
        return idProposta;
    }
}
