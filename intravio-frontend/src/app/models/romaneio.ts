import { Pedido } from "./pedido";

export interface RomaneioInput {
    id: string;
    pedidos: Number[];
    transportadorCodigo: string;
    taxaFrete: number;
    dataCriacao: Date;
    dataAtualizacao: Date;
    observacao: string;
    cnpj: number;
    isProcessa: boolean;
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


