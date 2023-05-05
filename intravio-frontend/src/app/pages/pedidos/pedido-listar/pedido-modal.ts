import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Pedido } from 'src/app/models/pedido';


@Component({
  selector: 'app-pedido-modal',
  template: `
    <mat-card>
      <mat-card-header>
        <mat-card-title>Pedido {{data.dataPedido | date : 'dd/MM/yyyy' }}-{{data.numeroPedido}}</mat-card-title>
      </mat-card-header>

      <mat-card-content>
        <div class="content">
              <div class="info-group">
                <mat-icon>receipt</mat-icon>
                <span class="info-group-label">Número do pedido: </span>
                <span>{{ data.numeroPedido }}</span>
              </div>

              <div class="info-group">
                <mat-icon>check_circle</mat-icon>
                <span class="info-group-label">Status do pedido: </span>
                <span>{{ retornaStatus(data.statusPedido) }}</span>
              </div>

              <div class="info-group">
                <mat-icon>person</mat-icon>
                <span class="info-group-label">Nome do remetente: </span>
                <span>{{ data.remetenteNome }}</span>
              </div>

              <div class="info-group">
                <mat-icon>email</mat-icon>
                <span class="info-group-label">E-mail do remetente: </span>
                <span>{{ data.remetenteEmail }}</span>
              </div>

              <div class="info-group">
                <mat-icon>person</mat-icon>
                <span class="info-group-label">Nome do destinatário: </span>
                <span>{{ data.destinatarioNome }}</span>
              </div>

              <div class="info-group">
                <mat-icon>email</mat-icon>
                <span class="info-group-label">E-mail do destinatário: </span>
                <span>{{ data.destinatarioEmail }}</span>
              </div>

              <div class="info-group">
                <mat-icon>room</mat-icon>
                <span class="info-group-label">Filial de Origem: </span>
                <span>{{ data.origem }}</span>
              </div>

              <div class="info-group">
                <mat-icon>room</mat-icon>
                <span class="info-group-label">Filial de Destino:  </span>
                <span>{{ data.destino }}</span>
              </div>

              <div class="info-group">
                <mat-icon>date_range</mat-icon>
                <span class="info-group-label">Data de criação do pedido:  </span>
                <span>{{ data.dataPedido | date:  'dd/MM/yyyy hh:mm:ss' }}</span>
              </div>

              <div class="info-group">
                <mat-icon>update</mat-icon>
                <span class="info-group-label">Data da última atualização:  </span>
                <span>{{ data.dataAtualizacao | date:  'dd/MM/yyyy hh:mm:ss' }}</span>
              </div>

              <div class="info-group">
                <mat-icon>priority_high</mat-icon>
                <span class="info-group-label">Prioridade:  </span>
                <span>{{ data.prioridade }}</span>
              </div>

              <div class="info-group">
                <mat-icon>new_releases</mat-icon>
                <span class="info-group-label">Acompanha status:  </span>
                <span>{{ data.acompanhaStatus }}</span>
              </div>

              <div class="info-group">
                <mat-icon>local_shipping</mat-icon>
                <span class="info-group-label">Código de rastreio: </span>
                <span>{{ data.codigoRastreio }}</span>
              </div>

              <div class="info-group">
                <mat-icon>fitness_center</mat-icon>
                <span class="info-group-label">Peso do Pedido: </span>
                <span>{{ data.pesoPedido }} kg</span>
              </div>
              <div class="info-group">
                <mat-icon>attach_money</mat-icon>
                <span class="info-group-label">Valor do Pedido: </span>
                <span>R$ {{ data.valorPedido }}</span>
              </div>
              <div class="info-group">
                  <mat-icon>shopping_cart</mat-icon>
                  <span class="info-group-label">Itens Pedido: </span>
                </div>
              <div class="table">
                <table class="table-responsive">
                    <thead>
                        <tr>
                            <th style="padding-left: 4px;">Nome</th>
                            <th style="padding-left: 4px;">Descrição</th>
                            <th style="text-align: center;">Quantidade</th>
                            <th style="text-align: center;">Preço</th>
                            <th style="text-align: center;">Peso</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr *ngFor="let item of data.itens; let i = index">
                            <td style="padding-left: 4px;">{{ item.produtoNome }}</td>
                            <td style="padding-left: 4px;">{{ item.descricao }}</td>
                            <td style="text-align: center;">{{ item.quantidade }}</td>
                            <td style="text-align: center;">R$ {{ item.subtotalPreco }}</td>
                            <td style="text-align: center;">{{ item.peso }} kg</td>
                        </tr>
                    </tbody>
                </table>
              </div>
          </div>
          </mat-card-content>
          <mat-card-actions>
            <button class="mat-elevation-z8 mb-2" mat-raised-button color="warn" mat-dialog-close>Fechar</button>
          </mat-card-actions>
        </mat-card>
`,
  styles: [`
      mat-card {
        display: flex;
        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        overflow-y: hidden !important;
      }
      
      mat-card-header {
        background-color: var(--color-primary);
        color: #ffffff;
      }
      
      mat-card-title {
        font-size: 20px;
        font-weight: bold;
        margin-right: 8px;
      }
      
      mat-card-subtitle {
        font-size: 14px;
        font-weight: bold;
      }
      
      mat-card-content {
        padding-top: 15px;
        padding-left: 20px;
        padding-right: 20px;
      }
      
      .content {
        max-height: 75vh;
        overflow-y: scroll;
        scroll-behavior: smooth;
        white-space: nowrap;
      }
      
      ::-webkit-scrollbar {
        width: 4px;
      }
      
      ::-webkit-scrollbar-thumb {
        background-color: var(---color-primary);
      }
      
      
      * {
        scrollbar-width: thin;
        scrollbar-color: var(--color-primary) #fff;
      }
      
      ::-webkit-scrollbar-track {
        background-color: #fff;
      }
      
      *::-webkit-scrollbar-thumb {
        background-color: var(--color-primary);
      }
      
      .info-group {
        display: flex;
        align-items: center;
        margin-bottom: 16px;
        margin-right: 15px;
      }
      
      .info-group mat-icon {
        margin-right: 12px;
        font-size: 20px;
        color: var(--color-secondary);
      }
      
      .info-group span {
        font-size: 16px;
        font-weight: 400;
      }
      
      mat-card-actions {
        display: flex;
        justify-content: flex-end;
      }
      
      mat-card-actions button {
        text-transform: uppercase;
        font-weight: bold;
      }
      
      mat-card-actions button:hover {
        cursor: pointer;
      }
      
      mat-card-actions button:focus {
        outline: none;
      }
      
      .info-group-label {
        font-weight: 600 !important;
        margin-right: 2px;
      }
      
      .table-responsive{
        padding-left: 35px;
      }
  `],
})
export class PedidoModal {

  constructor(@Inject(MAT_DIALOG_DATA) public data: Pedido) { }

  retornaStatus(status: Number): String {
    if (status === 0) {
      return "Pendente";
    } else if (status === 1) {
      return "Cancelado";
    } else if (status === 2) {
      return "Separado para Entrega";
    } else if (status === 3) {
      return "Entregue ao transportador";
    } else if (status === 4) {
      return "Pedido saiu para entrega";
    } else if (status === 5) {
      return "Em trânsito";
    } else if (status === 6) {
      return "Entregue";
    } else if (status === 7) {
      return "Retornado";
    } else {
      return "Recebido na cidade de destino";
    }
  };

}