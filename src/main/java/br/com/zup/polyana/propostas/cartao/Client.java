package br.com.zup.polyana.propostas.cartao;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class Client {

    public String buscaIpClient(HttpServletRequest request) {
        String ipClient = request.getHeader("X-FORWARDED-FOR"); //pega o ip de quem mandou a request
        if (ipClient == null) {
            ipClient = request.getRemoteAddr();                           //pega o ip de quem mandou a request
        }
        return ipClient;
    }

    public String buscaUserAgent(HttpServletRequest request) {
        return request.getHeader("USER-AGENT");
    }
}
