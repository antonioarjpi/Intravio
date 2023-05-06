import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Component, ViewChild } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { ThemePalette } from '@angular/material/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Pedido } from 'src/app/models/pedido';
import { RomaneioInput } from 'src/app/models/romaneio';
import { Transportador } from 'src/app/models/transportador';
import { PedidoService } from 'src/app/services/pedido.service';
import { RomaneioService } from 'src/app/services/romaneio.service';
import { TransportadorService } from 'src/app/services/transportador.service';

@Component({
  selector: 'app-romaneio-criar',
  templateUrl: './romaneio-criar.component.html',
  styleUrls: ['./romaneio-criar.component.css']
})
export class RomaneioCriarComponent {

  ELEMENT_DATA: Pedido[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  displayedColumns: string[] = ["acoes","numeroPedido", "remetenteNome", "destinatarioNome", "origem", "destino"];
  dataSource = new MatTableDataSource<Pedido>(this.ELEMENT_DATA);


  transportador: UntypedFormControl = new UntypedFormControl(null, Validators.required);
  transportadores: Transportador[];

  romaneio: RomaneioInput = {
    id: null,
    pedidos: [],
    transportadorCodigo: "",
    taxaFrete: null,
    dataCriacao: null,
    dataAtualizacao: null,
    observacao: "",
    cnpj: null,
    isProcessa: null,
  };

  constructor(
    private service: RomaneioService,
    private transportadorService: TransportadorService,
    private pedidoService: PedidoService,
    private toast: ToastrService,
    private router: Router,
    private _liveAnnouncer: LiveAnnouncer,
  ) { };

  ngOnInit(): void {
    this.transportadorService.findAll().subscribe(response => {
      this.transportadores = response;
    });

    this.listarTodosPedidos();
  };


  addPedido(obj: Number) {
    if (this.romaneio.pedidos.includes(obj)) {
      const index = this.romaneio.pedidos.indexOf(obj);
      this.romaneio.pedidos.splice(index, 1);
    } else {
      this.romaneio.pedidos.push(obj);
    }
  }

  criarRomaneio(): void {
    if (this.romaneio.pedidos.length < 1){
      this.toast.warning("Não é possível criar romaneio sem pedido", "Alerta" )
      return ;
    }
    this.service.create(this.romaneio).subscribe(
      (response) => {
        this.toast.success("Romaneio realizado com sucesso", "Cadastro");
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

  announceSortChange(sortState: Sort) {
    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  };

  listarTodosPedidos() {
    this.pedidoService.findAllByStatus(0).subscribe((response) => {
      this.ELEMENT_DATA = response;
      this.dataSource = new MatTableDataSource<Pedido>(response);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  };

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  };

  toggleLabel: string;

  retornaToogleLabel() {
    if (this.romaneio.isProcessa === true) {
      this.toggleLabel = "Sim";
    } else {
      this.toggleLabel = "Não";
    }
    console.log(this.romaneio.isProcessa)
  };

}
