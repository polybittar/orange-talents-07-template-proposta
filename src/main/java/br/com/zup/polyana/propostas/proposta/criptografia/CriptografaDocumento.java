package br.com.zup.polyana.propostas.proposta.criptografia;

import org.springframework.security.crypto.encrypt.Encryptors;

public class CriptografaDocumento {


    public static String encode(String text){

        return Encryptors.text("123456", "c0cc90fa7a5848048ca017bdcac19641").encrypt(text);
    }

    public static String decode(String text){

        return Encryptors.text("123456", "c0cc90fa7a5848048ca017bdcac19641").decrypt(text);
    }
}