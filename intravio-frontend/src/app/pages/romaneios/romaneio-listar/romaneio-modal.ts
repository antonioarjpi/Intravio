import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RomaneioGet } from 'src/app/models/romaneio';
import { Pedido } from 'src/app/models/pedido';

@Component({
  selector: 'app-romaneio-modal',
  template: `
    <mat-card>
      <mat-card-header>
        <mat-card-title>Romaneio {{data.dataCriacao | date : 'dd/MM/yyyy' }}-{{data.numeroRomaneio}}</mat-card-title>
      </mat-card-header>

      <mat-card-content>
        <div class="content">
              <div class="info-group">
                <div><mat-icon class="d-flex justify-content-center">receipt</mat-icon></div>
                <span class="info-group-label">Número do romaneio: </span>
                <span>{{ data.numeroRomaneio }}</span>
              </div>

              <div class="info-group">
                <div><mat-icon class="d-flex justify-content-center">check_circle</mat-icon></div>
                <span class="info-group-label">Status do romaneio: </span>
                <span>{{ retornaStatus(data.statusRomaneio) }}</span>
              </div>

              <div class="info-group">
                <div><mat-icon class="d-flex justify-content-center">local_shipping</mat-icon></div>
                <span class="info-group-label">Transportadora: </span>
                <span>{{ data.transportadora }}</span>
              </div>

              <div class="info-group">
                <div><mat-icon class="d-flex justify-content-center">directions_car</mat-icon></div>
                <span class="info-group-label">Motorista: </span>
                <span>{{ data.motorista }}</span>
              </div>

              <div class="info-group">
                <div><mat-icon class="d-flex justify-content-center">local_shipping</mat-icon></div>
                <span class="info-group-label">Veículo: </span>
                <span>{{ data.veiculo }}</span>
              </div>

              <div class="info-group">
                <div><mat-icon class="d-flex justify-content-center">drive_eta</mat-icon></div>
                <span class="info-group-label">Placa: </span>
                <span>{{ data.placa }}</span>
              </div>
              <div class="info-group">
                <div><mat-icon class="d-flex justify-content-center">date_range</mat-icon></div>
                <span class="info-group-label">Data de criação do romaneio:  </span>
                <span>{{ data.dataCriacao | date:  'dd/MM/yyyy hh:mm:ss' }}</span>
              </div>

              <div class="info-group">
                <div><mat-icon class="d-flex justify-content-center">update</mat-icon></div>
                <span class="info-group-label">Data da última atualização:  </span>
                <span>{{ data.dataAtualizacao | date:  'dd/MM/yyyy hh:mm:ss' }}</span>
              </div>

              <div class="info-group">
                <div><mat-icon class="d-flex justify-content-center">attach_money</mat-icon></div>
                <span class="info-group-label">Valor do Frete: </span>
                <span>R$ {{ data.taxaFrete }}</span>
              </div>

              <div class="info-group">
                <div><mat-icon class="d-flex justify-content-center">fitness_center</mat-icon></div>
                <span class="info-group-label">Peso do Romaneio: </span>
                <span>{{ data.pesoCarga }} kg</span>
              </div>

              <div class="info-group">
                <div><mat-icon class="d-flex justify-content-center">attach_money</mat-icon></div>
                <span class="info-group-label">Valor do Romaneio: </span>
                <span>R$ {{ data.valorCarga }}</span>
              </div>

              <div class="info-group">
                  <div><mat-icon class="d-flex justify-content-center">assignment</mat-icon></div>
                  <span class="info-group-label">Itens Romaneio: </span>
                </div>
              <div class="table">
                <table class="table-responsive">
                    <thead>
                        <tr>
                            <th>N° Pedido</th>
                            <th>QNT Itens</th>
                            <th>Preço</th>
                            <th>Peso</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr *ngFor="let item of retornaPedidosOrdenado(); let i = index">
                            <td>{{ item.numeroPedido }}</td>
                            <td>{{ item.itens.length }}</td>
                            <td>R$ {{ item.valorPedido }}</td>
                            <td>{{ item.pesoPedido }} kg</td>
                        </tr>
                    </tbody>
                </table>
              </div>

              <div class="info-group">
                <div><mat-icon class="d-flex justify-content-center">new_releases</mat-icon></div>
                <span class="info-group-label">Observações </span>
                <span>{{ data.observacao }}</span>
              </div>
          </div>
          </mat-card-content>
          <mat-card-actions>
            <button class="mat-elevation-z8 mr-3 mb-2" mat-raised-button color="warn" mat-dialog-close>Fechar</button>
          </mat-card-actions>
        </mat-card>
`,
  styles: [`
      mat-card {
        display: flex;
        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        overflow-y: hidden !important;
        min-width: 300px;
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
      
      }
      
      .content {
        max-height: 75vh;
        overflow-y: scroll;
        scroll-behavior: smooth;
        white-space: nowrap;
        padding-left: 10px;
        padding-right: 30px;
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

      @media only screen and (max-width: 600px) {
        .info-group {
        flex-wrap: wrap;
        flex-direction: column;
        margin-bottom:18px
      }

      .content{
        padding-right: 10px;
        padding-left: 10px;
      }

      .info-group mat-icon {
        margin: 0 !important;
        padding: 0 !important;
        font-size: 30px;
        color: var(--color-secondary);
      }
    }
      
      .info-group mat-icon {
        margin-right: 12px;
        font-size: 22px;
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
export class RomaneioModal {

  constructor(@Inject(MAT_DIALOG_DATA) public data: RomaneioGet) { }

  retornaPedidosOrdenado(): Pedido[]{
  let novoArray = this.data.pedidos.sort((a, b) => a.numeroPedido - b.numeroPedido);
  return novoArray;
  }

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
      return "Romaneio saiu para entrega";
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