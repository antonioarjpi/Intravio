import { Produto } from "./produto";

export interface Item {
    produto: Produto;
    descricao: string;
    quantidade: number;
}

export interface ItemGet{
    produtoNome: String;
    descricao: String;
    quantidade: Number;
    subtotalPreco: Number;
    peso: Number;
}