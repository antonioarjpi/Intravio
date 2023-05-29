package com.intraviologistica.intravio.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepartamentoTest {

    public static Departamento getDepartamento1() {
        return new Departamento("141aba2", "Financeiro");
    }

    public static Departamento getDepartamento2() {
        Departamento departamento = new Departamento();
        departamento.setId("4ba256a");
        departamento.setNome("Comercial");
        return departamento;
    }

    @Test
    public void testToString() {
        Departamento departamento = getDepartamento1();

        String expected = "Departamento{id='141aba2', nome='Financeiro'}";
        String result = departamento.toString();

        assertEquals(expected, result);
    }
}