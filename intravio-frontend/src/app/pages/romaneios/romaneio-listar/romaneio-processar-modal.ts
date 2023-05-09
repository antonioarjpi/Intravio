import { Component, EventEmitter, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RomaneioGet } from 'src/app/models/romaneio';
import { Pedido } from 'src/app/models/pedido';
import { RomaneioService } from 'src/app/services/romaneio.service';
import { Toast, ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-romaneio-modal',
  template: `
<mat-card>
    <mat-card-header>
        <mat-card-title>Processamento romaneio {{data.dataCriacao | date:
            'dd/MM/yyyy'}}-{{data.numeroRomaneio}}</mat-card-title>
    </mat-card-header>
    <mat-card-content>
        <div class="content">
            <label><strong>Atenção:</strong> Este romaneio será processado e atualizará todos os pedidos para status "Em trânsito". Você confirma o processamento desse romaneio?
            </label>
        </div>
    </mat-card-content>
    <mat-card-actions>
        <button class="mat-elevation-z8 mb-2" (click)="processarRomaneio(data.id)" mat-raised-button color="warn"
            mat-dialog-close>Confirmar processamento</button>
        <button class="mat-elevation-z8 mb-2" mat-raised-button color="basic" mat-dialog-close>Cancelar</button>
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
        display: flex;
        max-height: 75vh;
        overflow-y: scroll;
        scroll-behavior: smooth;
        padding-left: 30px;
        padding-right: 30px;
        margin: 40px 0 40px 0;
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
        margin: 20px 0 50px 0;
      }

      .info-group mat-icon {
        margin: 0 !important;
        padding: 0 !important;
        font-size: 30px;
        color: var(--color-secondary);
      }

      mat-card-actions {
        display: flex;
        justify-content: center !important;
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
        flex-wrap: wrap
      }
      
      mat-card-actions button {
        text-transform: uppercase;
        font-weight: bold;
        padding: 20px 9px 20px 9px;
        margin-left: 8px;
      }
      
      mat-card-actions button:hover {
        cursor: pointer;
      }
      
      mat-card-actions button:focus {
        outline: none;
      }
      
  `],
})
export class RomaneioProcessarModal {

  romaneio = new EventEmitter<void>();

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: RomaneioGet,
    private service: RomaneioService,
    private toast: ToastrService,
    ) { }

  processarRomaneio(id: string){
    this.service.processarRomaneio(id).subscribe(response => {
        this.toast.success("Romaneio processado", "Processamento");
        this.romaneio.emit()
    })
  }

}