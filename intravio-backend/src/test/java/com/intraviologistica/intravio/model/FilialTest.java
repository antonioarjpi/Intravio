package com.intraviologistica.intravio.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilialTest {

    public static Filial getFilial1() {
        return new Filial(1l, "Filial Alpha", EnderecoTest.getEndereco1());
    }

    public static Filial getFilial2() {
        return new Filial(2l, "Filial Beta", EnderecoTest.getEndereco2());
    }

    @Test
    public void testToString() {
        Filial filial = getFilial1();

        String expected = "Filial{id=1, nome='Filial Alpha', endereco=" + filial.getEndereco().toString() + "}";
        String actual = filial.toString();
        assertEquals(expected, actual);
    }
}