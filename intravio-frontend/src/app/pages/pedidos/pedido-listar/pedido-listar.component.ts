import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Pedido } from 'src/app/models/pedido';
import { PedidoService } from 'src/app/services/pedido.service';
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { MatSort, Sort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { PedidoModal } from './pedido-modal';
import { PedidoDeletarComponent } from '../pedido-deletar/pedido-deletar.component';

@Component({
  selector: 'app-pedido-listar',
  templateUrl: './pedido-listar.component.html',
  styleUrls: ['./pedido-listar.component.css']
})

export class PedidoListarComponent implements OnInit {
  ELEMENT_DATA: Pedido[] = [];
  FILTERED_DATA: Pedido[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  displayedColumns: string[] = ["numeroPedido", "numeroRomaneio", "prioridade", "destinatarioNome", "destino", "statusPedido", "acoes"];
  dataSource = new MatTableDataSource<Pedido>(this.ELEMENT_DATA);

  exibeFiltros: boolean = false;
  buscaValor: Number;
  tipoBusca: string = 'pedido';
  minDate: string = '';
  maxDate: string = '';
  isInputFocused: boolean = false;
  isInputFocused2: boolean = false;

  constructor(
    private service: PedidoService,
    private _liveAnnouncer: LiveAnnouncer,
    private dialog: MatDialog) { }

  ngOnInit(): void {

    const dataInicial = new Date();
    dataInicial.setDate(dataInicial.getDate() - 7);
    this.minDate = dataInicial.toISOString().substring(0, 10);
    this.maxDate = new Date().toISOString().substring(0, 10);

    this.listarTodosPedidos();
  }

  announceSortChange(sortState: Sort) {
    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  };

  listarTodosPedidos() {
    this.service.findAll(this.minDate, this.maxDate).subscribe((response) => {
      this.ELEMENT_DATA = response;
      this.dataSource = new MatTableDataSource<Pedido>(response);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  };

  baixarImagens(id) {
    this.service.baixarImagens(id).subscribe((response: Blob) => {
      const url = window.URL.createObjectURL(response);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'arquivos.zip';
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
    })
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  };

  openModalView(pedido) {
    this.dialog.open(PedidoModal, { data: pedido });
    const card = document.querySelector('.content');
    card.scrollTop = 0;
  };

  openModalDelete(pedido) {
    const dialogRef = this.dialog.open(PedidoDeletarComponent, { data: pedido });
    const card = document.querySelector('.content');
    card.scrollTop = 0;

    dialogRef.componentInstance.pedidoCancelado.subscribe(() => {
      this.listarTodosPedidos();
    });
  };

  orderByStatus(status: any): void {
    let list: Pedido[] = [];
    this.ELEMENT_DATA.forEach(element => {
      if (element.statusPedido == status)
        list.push(element)
    });

    this.recarregaLista(list);
  }

  orderByPrioridade(prioridade: any): void {
    let list: Pedido[] = [];
    this.ELEMENT_DATA.forEach(element => {
      if (element.prioridade == prioridade)
        list.push(element)
    });

    this.recarregaLista(list);
  }

  pesquisaAvancada() {
    let list: Pedido[] = [];
    let chave: string;

    if (this.tipoBusca === 'pedido') {
      chave = 'numeroPedido';
    } else {
      chave = 'numeroRomaneio';
    }

    if (this.buscaValor == 0 || this.buscaValor == null) {
      this.listarTodos();
      return;
    }

    this.ELEMENT_DATA.forEach(element => {
      if (element[chave] == this.buscaValor) {
        list.push(element);
      }
    });

    this.recarregaLista(list);
  }

  filtrarPedidos(status: any, prioridade: any): void {
    let list: Pedido[] = [];

    this.ELEMENT_DATA.forEach(element => {
      const atendeStatus = status === null || element.statusPedido === status;
      const atendePrioridade = prioridade === null || element.prioridade === prioridade;

      if (atendeStatus && atendePrioridade) {
        list.push(element);
      }
    });

    this.recarregaLista(list);
  }

  listarTodos() {
    this.recarregaLista(this.ELEMENT_DATA)
  }

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
  };

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
      return "Pedido saiu para entrega";
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

  private recarregaLista(list: any) {
    this.FILTERED_DATA = list;
    this.dataSource = new MatTableDataSource<Pedido>(list);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
}