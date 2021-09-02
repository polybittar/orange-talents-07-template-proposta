package br.com.zup.polyana.propostas.viagem;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "viagemCartoes", url = "http://localhost:8888/api/cartoes/")
public interface ViagemCartaoClient {

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/{numeroCartao}/avisos")
    ViagemResponse avisoViagemCartao(@PathVariable("numeroCartao") String numeroCartao, @RequestBody ViagemRequest avisoViagemRequest);
}