package br.com.zup.polyana.propostas.proposta;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@FeignClient(value = "solicitacao", url = "${solicitacao.host}")
public interface SolicitacaoAnaliseClient {

    @RequestMapping(method = RequestMethod.POST)
    SolicitacaoAnaliseResponse enviaSolicitacaoAnalise(@RequestBody @Valid SolicitacaoAnaliseRequest request);
}