import { Component } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { forkJoin } from 'rxjs';
import { Pedido } from 'src/app/models/pedido';
import { RomaneioInput } from 'src/app/models/romaneio';
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

  displayedColumns: string[] = ["entregue","devolvido", "numeroPedido", "remetenteNome", "destinatarioNome", "origem", "destino"];
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
    this.romaneio.id = this.route.snapshot.paramMap.get("id")

    this.transportadorService.findAll().subscribe(response => {
      this.transportadores = response;
    });

    this.buscarPedidoPorId();
  };

  buscarPedidoPorId(): void {
    this.service.findById(this.romaneio.id).subscribe(response => {
      this.romaneio = response;
      this.listarPedidosDoRomaneio();
      for (let i = 0; i < response.pedidos.length; i++) {
        this.addPedido(response[i].numeroPedido);
      }
    })
  }

  addPedido(obj: Number) {
    if (this.romaneio.pedidos.includes(obj)) {
      const index = this.romaneio.pedidos.indexOf(obj);
      this.romaneio.pedidos.splice(index, 1);
    } else {
      this.romaneio.pedidos.push(obj);
    }
  }

  atualizarRomaneio(): void {
    if (this.romaneio.pedidos.length < 1) {
      this.toast.warning("Não é possível criar romaneio sem pedido", "Alerta")
      return;
    }

    this.romaneio.taxaFrete = parseFloat(this.romaneio.taxaFrete.toString().replace(".", "").replace(",", "."));

    this.service.update(this.romaneio).subscribe(
      (response) => {
        this.toast.success("Romaneio atualizado com sucesso", "Atualização");
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
    this.pedidoService.findAllByRomaneio(this.romaneio.id).subscribe(response => {
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
