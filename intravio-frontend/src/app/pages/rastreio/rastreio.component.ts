import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { HistoricoPedido } from 'src/app/models/HistoricoPedido';
import { PedidoService } from 'src/app/services/pedido.service';

@Component({
  selector: 'app-rastreio',
  templateUrl: './rastreio.component.html',
  styleUrls: ['./rastreio.component.css']
})
export class RastreioComponent implements OnInit {

  historicoPedidos: HistoricoPedido[] = [];
  codigoRastreio: String = "";

  constructor(
    private pedidoService: PedidoService,
    private toast: ToastrService
  ) { }

  ngOnInit(): void {
  }

  buscarRastreamento() {
    if (this.codigoRastreio === null || this.codigoRastreio.length < 3) {
      this.toast.error("Insira o código de rastreio válido");
      return;
    }

    this.pedidoService.exibirHistoricoPedidos(this.codigoRastreio).subscribe(response => {
      this.historicoPedidos = response;
    }, (ex) => {
      this.toast.error(ex.error.message);
    })
  }
}
