import { Item, ItemGet } from "./item";

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
    itens: ItemGet[];
    numeroPedido: number;
    statusPedido: any;
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