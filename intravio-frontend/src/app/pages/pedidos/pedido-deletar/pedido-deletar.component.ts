import { Component, EventEmitter, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { Pedido } from 'src/app/models/pedido';
import { PedidoService } from 'src/app/services/pedido.service';

@Component({
  selector: 'app-pedido-deletar',
  templateUrl: './pedido-deletar.component.html',
  styleUrls: ['./pedido-deletar.component.css']
})

export class PedidoDeletarComponent {

  motivo: string;

  pedidoCancelado = new EventEmitter<void>();

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: Pedido,
    private service: PedidoService,
    private toast: ToastrService,
  ) { }

  cancelarPedido(id) {
    this.service.delete(id, this.motivo).subscribe(() => {
      this.toast.success("Pedido cancelado com sucesso", "Cancelamento");
      setTimeout(() => {
        this.pedidoCancelado.emit();
      }, 500)
    }), (ex) => {
      this.toast.error(ex.error.message, "Erro")
    }
  }
}
