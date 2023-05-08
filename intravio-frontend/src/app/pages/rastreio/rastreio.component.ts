import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
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
  codigoRastreio: string;
  captcha: string;

  constructor(
    private pedidoService: PedidoService,
    private toast: ToastrService
  ) { }

  ngOnInit(): void {
    this.codigoRastreio = 'YYO85755748YOR'
  }

  buscarRastreamento() {
    this.pedidoService.exibirHistoricoPedidos(this.codigoRastreio).subscribe(response => {
      this.historicoPedidos = response;
    }, (ex) => {
      this.toast.error(ex.error.message);
    })

  }

}
