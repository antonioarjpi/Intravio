import { Component } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Pedido } from 'src/app/models/pedido';
import { RomaneioFechamento, RomaneioInput } from 'src/app/models/romaneio';
import { Transportador } from 'src/app/models/transportador';
import { PedidoService } from 'src/app/services/pedido.service';
import { RomaneioService } from 'src/app/services/romaneio.service';
import { TransportadorService } from 'src/app/services/transportador.service';

@Component({
  selector: 'app-romaneio-fechamento',
  templateUrl: './romaneio-fechamento.component.html',
  styleUrls: ['./romaneio-fechamento.component.css']
})
export class RomaneioFechamentoComponent {

  ELEMENT_DATA: Pedido[] = [];
  pedidos: Pedido[] = [];
  transportador: UntypedFormControl = new UntypedFormControl(null, Validators.required);
  transportadores: Transportador[];
  checked = false;

  romaneioFechamento: RomaneioFechamento = {
    romaneioId: "",
    pedidosConcluido: [],
    pedidosRetorno: [],
  };

  romaneio: RomaneioInput = {
    id: null,
    numeroRomaneio: null,
    pedidos: [],
    transportadorCodigo: "",
    taxaFrete: null,
    dataCriacao: null,
    dataAtualizacao: null,
    observacao: "",
    cnpj: null,
    processa: null,
  };

  displayedColumns: string[] = ["entregue", "devolvido", "numeroPedido", "remetenteNome", "destinatarioNome", "origem", "destino"];
  dataSource = new MatTableDataSource<Pedido>(this.ELEMENT_DATA);

  constructor(
    private service: RomaneioService,
    private transportadorService: TransportadorService,
    private pedidoService: PedidoService,
    private toast: ToastrService,
    private router: Router,
    private route: ActivatedRoute
  ) { };

  ngOnInit(): void {
    this.romaneioFechamento.romaneioId = this.route.snapshot.paramMap.get("id")

    this.transportadorService.findAll().subscribe((response) => {
      this.transportadores = response;
    });

    this.buscarPedidoPorId();
  };

  buscarPedidoPorId(): void {
    this.service.findById(this.romaneioFechamento.romaneioId).subscribe((response) => {
      this.romaneio = response;
      this.listarPedidosDoRomaneio();
      this.romaneioFechamento.pedidosConcluido = response.pedidos
    })
  }

  addPedidoConcluido(obj: Number) {
    if (this.romaneioFechamento.pedidosConcluido.includes(obj)) {
      const index = this.romaneioFechamento.pedidosConcluido.indexOf(obj);
      this.romaneioFechamento.pedidosConcluido.splice(index, 1);

      this.romaneioFechamento.pedidosRetorno.push(obj);

    } else {
      const index = this.romaneioFechamento.pedidosRetorno.indexOf(obj);
      this.romaneioFechamento.pedidosRetorno.splice(index, 1);

      this.romaneioFechamento.pedidosConcluido.push(obj);
    }
  }

  addPedidoRetorno(obj: Number) {
    if (this.romaneioFechamento.pedidosRetorno.includes(obj)) {
      const index = this.romaneioFechamento.pedidosRetorno.indexOf(obj);
      this.romaneioFechamento.pedidosRetorno.splice(index, 1);

      this.romaneioFechamento.pedidosConcluido.push(obj);

    } else {
      const index = this.romaneioFechamento.pedidosConcluido.indexOf(obj);
      this.romaneioFechamento.pedidosConcluido.splice(index, 1);

      this.romaneioFechamento.pedidosRetorno.push(obj);
    }
  }

  atualizarRomaneio(): void {
    this.service.fecharRomaneio(this.romaneioFechamento).subscribe(() => {
      this.toast.success("Romaneio fechado com sucesso", "Fechamento");
      this.router.navigate(["romaneios"]);
    },
      (ex) => {
        if (ex.error.errors) {
          ex.error.errors.forEach((element) => {
            this.toast.error(element.message);
          });
        } else {
          this.toast.error(ex.error.message);
        }
      }
    )
  };

  listarPedidosDoRomaneio() {
    this.pedidoService.findAllByRomaneio(this.romaneioFechamento.romaneioId).subscribe((response) => {
      this.atualizarTabela(response.sort((a, b) => a.numeroPedido - b.numeroPedido));
    })
  };

  atualizarTabela(pedidos: Pedido[]) {
    this.ELEMENT_DATA = pedidos;
    this.dataSource = new MatTableDataSource<Pedido>(pedidos);
  }

  marcarTodos() {
    if (!this.checked) {
      this.romaneio.pedidos = [];
    } else {
      for (let i = 0; i < this.ELEMENT_DATA.length; i++) {
        let obj = this.ELEMENT_DATA[i].numeroPedido;
        this.romaneio.pedidos.push(obj);
      }
    }
  }
}
