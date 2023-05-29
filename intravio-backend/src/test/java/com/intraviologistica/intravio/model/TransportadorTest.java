package com.intraviologistica.intravio.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransportadorTest {

    public static Transportador getTransportador() {
        Transportador transportador = new Transportador();
        transportador.setId("1ab2c-42adc");
        transportador.setNome("Transportadora Teste");
        transportador.setCnpj("00.000.000/0001-00");
        transportador.setMotorista("Edvaldo");
        transportador.setPlaca("PCX-1234");
        transportador.setVeiculo("Mercedes-Benz");
        transportador.setObservacao("Observacao");
        return transportador;
    }

    @Test
    public void testToString() {
        String expected = "Transportador{id='1ab2c-42adc', nome='Transportadora Teste', motorista='Edvaldo', placa='PCX-1234', veiculo='Mercedes-Benz', observacao='Observacao', cnpj='00.000.000/0001-00'}";
        String actual = getTransportador().toString();

        assertEquals(expected, actual);
    }
}

