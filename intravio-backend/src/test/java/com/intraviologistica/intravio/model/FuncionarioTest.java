package com.intraviologistica.intravio.model;

public class FuncionarioTest {

    public static Funcionario getFuncionario1() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId("51ab12cd54");
        funcionario.setNome("João");
        funcionario.setEmail("joao@teste.com");
        funcionario.setDepartamento(null);
        funcionario.setFilial(null);
        return funcionario;
    }

    public static Funcionario getFuncionario2() {
        Funcionario maria = new Funcionario();
        maria.setId("ab231a1s");
        maria.setNome("Maria");
        maria.setEmail("maria@teste.com");
        maria.setDepartamento(null);
        maria.setFilial(null);
        return maria;
    }

}