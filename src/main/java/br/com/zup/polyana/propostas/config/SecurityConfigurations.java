package br.com.zup.polyana.propostas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    //autorização
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests(authorizeRequests ->
                authorizeRequests
                        .antMatchers(HttpMethod.POST,"/api/proposta/**").hasAuthority("SCOPE_meu-primeiro-escopo")
                        .antMatchers(HttpMethod.GET,"/actuator/**").hasAuthority("SCOPE_meu-primeiro-escopo")
                        .antMatchers(HttpMethod.POST,"/api/cartao/**").hasAuthority("SCOPE_meu-primeiro-escopo")
                        .antMatchers(HttpMethod.GET,"/api/proposta/**").hasAuthority("SCOPE_meu-primeiro-escopo")
                        .anyRequest().authenticated()
        ).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

    }
}