package com.intraviologistica.intravio.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnderecoTest {

    public static Endereco getEndereco1() {
        Endereco endereco = new Endereco();
        endereco.setId("11189");
        endereco.setBairro("Centro");
        endereco.setRua("Rua Principal");
        endereco.setCep("12345-678");
        endereco.setNumero(10);
        endereco.setComplemento("Sala 101");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        return endereco;
    }

    public static Endereco getEndereco2() {
        Endereco endereco = new Endereco();
        endereco.setId("12345");
        endereco.setBairro("Subúrbio");
        endereco.setRua("Rua Secundária");
        endereco.setCep("54321-098");
        endereco.setNumero(50);
        endereco.setComplemento("Apartamento 202");
        endereco.setCidade("Piauí");
        endereco.setEstado("Teresina");
        return endereco;
    }

    @Test
    public void testToString() {
        Endereco endereco = getEndereco1();

        String expected = "Endereco{id='11189', rua='Rua Principal', cep='12345-678', bairro='Centro', numero='10', complemento='Sala 101', estado='SP', cidade='São Paulo'}";
        String result = endereco.toString();

        assertEquals(expected, result);
    }
}