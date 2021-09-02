package br.com.zup.polyana.propostas.carteira;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "associaCarteira", url = "${cartoes.host}")
public interface CarteiraCartaoClient {

    @RequestMapping(method = RequestMethod.POST,consumes = "application/json",value = "/{idCartao}/carteiras")
    CarteiraResponse associaCarteiraDigital(@RequestParam(required = true) String idCartao,
                                              @RequestBody CarteiraRequest carteiraRequest);
}