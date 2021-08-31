package br.com.zup.polyana.propostas.bloqueio;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "bloqueioCartao", url = "http://localhost:8888/api/cartoes")
public interface BloqueioCartaoClient {

    @RequestMapping(method = RequestMethod.POST, value = "/{idCartao}/bloqueios", consumes = "application/json")
    String bloqueiaCartao(@RequestParam(required = true,name = "idCartao") String id, @RequestBody Map<String,String> sistema);

}