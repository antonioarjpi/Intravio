import { Item } from "./item";

export interface PedidoInput {
    id: String;
    itens: Item[];
    fotos: string[];
    origem: String;
    destino: String;
    remetente: String;
    destinatario: String;
    prioridade: any;
    acompanhaStatus: Number;
    numeroPedido: Number;
};

export interface Pedido {
    id:String;
    numeroPedido: Number;
    statusPedido: String;
    remetenteNome: String;
    remetenteEmail: String;
    destinatarioNome: String;
    destinatarioEmail: String;
    origem: String;
    destino: String;
    dataPedido: Date;
    dataAtualizacao: Date;
    prioridade: Number;
    acompanhaStatus: Number;
    codigoRastreio: String;
    pesoPedido: Number;
    valorPedido: Number;
}