import { Pedido } from "./pedido";

export interface RomaneioInput {
    id: string;
    pedidos: Number[];
    numeroRomaneio: number;
    transportadorCodigo: string;
    taxaFrete: number;
    dataCriacao: Date;
    dataAtualizacao: Date;
    observacao: string;
    cnpj: number;
    processa: boolean;
}

export interface RomaneioGet {
    id: string;
    numeroRomaneio: number;
    transportadora: string;
    placa: string;
    veiculo: string;
    motorista: string;
    statusRomaneio: number;
    taxaFrete: number;
    dataCriacao: Date;
    dataAtualizacao: Date;
    observacao: string;
    quantidadePedidos: number;
    pesoCarga: number;
    valorCarga: number;
    pedidos: Pedido[];
}

export interface RomaneioFechamento{
    romaneioId: string;
    pedidosConcluido: Number[];
    pedidosRetorno: Number[];
}

