package br.com.zup.polyana.propostas.cartao;

import br.com.zup.polyana.propostas.carteira.CarteiraRequest;
import br.com.zup.polyana.propostas.carteira.CarteiraResponse;
import br.com.zup.polyana.propostas.viagem.ViagemRequest;
import br.com.zup.polyana.propostas.viagem.ViagemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "cartao", url = "${cartoes.host}")
public interface AnaliseCartaoClient {

    @RequestMapping(method = RequestMethod.GET)
    AnaliseCartaoResponse buscaAnaliseCartao(@RequestBody @RequestParam(required = true,name = "idProposta") String idProposta);

    @RequestMapping(method = RequestMethod.POST, value = "/{idCartao}/bloqueios", consumes = "application/json")
    String bloqueiaCartao(@RequestParam(required = true,name = "idCartao") String idCartao, @RequestBody Map<String,String> sistema);

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/{idCartao}/avisos")
    ViagemResponse avisoViagemCartao(@PathVariable("idCartao") String idCartao, @RequestBody ViagemRequest avisoViagemRequest);

    @RequestMapping(method = RequestMethod.POST,consumes = "application/json",value = "/{idCartao}/carteiras")
    CarteiraResponse associaCarteiraDigital(@RequestParam(required = true) String idCartao,
                                            @RequestBody CarteiraRequest carteiraRequest);

}