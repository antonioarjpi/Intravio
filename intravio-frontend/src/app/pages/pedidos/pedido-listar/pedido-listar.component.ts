import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Pedido } from 'src/app/models/pedido';
import { PedidoService } from 'src/app/services/pedido.service';
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { MatSort, Sort } from '@angular/material/sort';

@Component({
  selector: 'app-pedido-listar',
  templateUrl: './pedido-listar.component.html',
  styleUrls: ['./pedido-listar.component.css']
})
export class PedidoListarComponent implements OnInit {
  ELEMENT_DATA: Pedido[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  displayedColumns: string[] = ["numeroPedido", "prioridade", "remetenteNome", "destinatarioNome", "origem", "destino", "status", "acoes"];
  dataSource = new MatTableDataSource<Pedido>(this.ELEMENT_DATA);

  constructor(
    private service: PedidoService,
    private _liveAnnouncer: LiveAnnouncer) { }

  ngOnInit(): void {
    this.listarTodosPedidos();
  }

  ngAfterViewInit() { }

  announceSortChange(sortState: Sort) {
    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  }

  listarTodosPedidos() {
    this.service.findAll().subscribe((response) => {
      this.ELEMENT_DATA = response;
      this.dataSource = new MatTableDataSource<Pedido>(response);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  };

  retornaPrioridade(status: any): string {
    if (status == "0") {
      return "BAIXA";
    } else if (status == "1") {
      return "MÉDIA";
    } else if (status == "2") {
      return "ALTA";
    } else {
      return "URGENTE"
    }
  }

}