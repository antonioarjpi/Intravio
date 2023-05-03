import { Produto } from "./produto";

export interface Item {
    produto: Produto;
    descricao: string;
    quantidade: number;
}