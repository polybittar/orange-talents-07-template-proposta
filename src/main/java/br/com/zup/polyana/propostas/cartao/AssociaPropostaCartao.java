package br.com.zup.polyana.propostas.cartao;

import br.com.zup.polyana.propostas.proposta.EstadoProposta;
import br.com.zup.polyana.propostas.proposta.Proposta;
import br.com.zup.polyana.propostas.proposta.PropostaRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AssociaPropostaCartao {

    private PropostaRepository propostaRepository;
    private AnaliseCartaoClient analiseCartao;

    public AssociaPropostaCartao(PropostaRepository propostaRepository, AnaliseCartaoClient analiseCartao) {
        this.propostaRepository = propostaRepository;
        this.analiseCartao = analiseCartao;
    }

    private final Logger logger = LoggerFactory.getLogger(AssociaPropostaCartao.class);

    @Scheduled(fixedRateString = "${periodicidade.associa-cartao}")
    //associa cartão à proposta quando ela ainda não tem cartão e for elegível
    public void AssociaPropostaECartao()  throws Exception {

        Iterable<Proposta> propostasElegiveis =
                propostaRepository.findByEstadoPropostaAndCartaoIsNull(EstadoProposta.ELEGÍVEL);

        propostasElegiveis.forEach(proposta->{
            try {

                AnaliseCartaoResponse result =
                        analiseCartao.buscaAnaliseCartao(proposta.getId().toString());
                proposta.associaCartao(result.getId());
                propostaRepository.save(proposta);
                logger.info("Cartão associado à proposta com sucesso");

            } catch (FeignException e) {
                logger.info("Não foi possível associar o cartão à proposta");
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Não foi possível associar o cartão à proposta");
            }

        });
    }

}