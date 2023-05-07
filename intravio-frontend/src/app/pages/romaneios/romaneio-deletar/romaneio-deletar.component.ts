import { Component } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Pedido } from 'src/app/models/pedido';
import { RomaneioInput } from 'src/app/models/romaneio';
import { Transportador } from 'src/app/models/transportador';
import { PedidoService } from 'src/app/services/pedido.service';
import { RomaneioService } from 'src/app/services/romaneio.service';

@Component({
  selector: 'app-romaneio-deletar',
  templateUrl: './romaneio-deletar.component.html',
  styleUrls: ['./romaneio-deletar.component.css']
})
export class RomaneioDeletarComponent {


  ELEMENT_DATA: Pedido[] = [];
  pedidos: Pedido[] = [];
  transportador: UntypedFormControl = new UntypedFormControl(null, Validators.required);
  transportadores: Transportador[];

  romaneio: RomaneioInput = {
    id: null,
    pedidos: [],
    numeroRomaneio: null,
    transportadorCodigo: "",
    taxaFrete: null,
    dataCriacao: null,
    dataAtualizacao: null,
    observacao: "",
    cnpj: null,
    processa: null,
  };

  displayedColumns: string[] = ["numeroPedido", "remetenteNome", "destinatarioNome", "origem", "destino"];
  dataSource = new MatTableDataSource<Pedido>(this.ELEMENT_DATA);

  constructor(
    private service: RomaneioService,
    private pedidoService: PedidoService,
    private toast: ToastrService,
    private router: Router,
    private route: ActivatedRoute
  ) { };

  ngOnInit(): void {
    this.romaneio.id = this.route.snapshot.paramMap.get("id")
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

  deletarRomaneio(): void {
    this.service.delete(this.romaneio).subscribe(
      (response) => {
        this.toast.success("Romaneio deletado com sucesso", "Exclusão");
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
    this.pedidoService.findAllByRomaneio(this.romaneio.id).subscribe((response) => {
      this.atualizarTabela(response);
    });
  };

  atualizarTabela(response: Pedido[]) {
    this.ELEMENT_DATA = response;
    this.dataSource = new MatTableDataSource<Pedido>(response);
  }
}
