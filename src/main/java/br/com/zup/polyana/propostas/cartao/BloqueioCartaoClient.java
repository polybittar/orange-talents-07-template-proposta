package br.com.zup.polyana.propostas.cartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(value = "bloqueioCartao", url = "http://localhost:8888/api/cartoes")
public interface BloqueioCartaoClient {

    @RequestMapping(method = RequestMethod.POST, value = "/{id}/bloqueios", consumes = "application/json")
    String bloqueiaCartao(@PathVariable String id, @RequestBody Map<String,String> sistema);

}