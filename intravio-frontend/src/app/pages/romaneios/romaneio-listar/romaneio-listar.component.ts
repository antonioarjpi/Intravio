import { Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { MatSort, Sort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { RomaneioGet } from 'src/app/models/romaneio';
import { RomaneioService } from 'src/app/services/romaneio.service';
import { RomaneioModal } from './romaneio-modal';
import { RomaneioProcessarModal } from './romaneio-processar-modal';

@Component({
  selector: 'app-romaneio-listar',
  templateUrl: './romaneio-listar.component.html',
  styleUrls: ['./romaneio-listar.component.css']
})
export class RomaneioListarComponent {
  ELEMENT_DATA: RomaneioGet[] = [];
  FILTERED_DATA: RomaneioGet[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  displayedColumns: string[] = ["numeroRomaneio", "quantidadePedidos", "valorCarga", "pesoCarga", "motorista", "transportadora", "statusRomaneio", "acoes"];
  dataSource = new MatTableDataSource<RomaneioGet>(this.ELEMENT_DATA);
  abrePesquisa: boolean = false;

  exibeFiltros: boolean = false;
  numeroRomaneio: number = null;
  minDate: string = '';
  maxDate: string = '';
  isInputFocused: boolean = false;
  isInputFocused2: boolean = false;

  constructor(
    private service: RomaneioService,
    private _liveAnnouncer: LiveAnnouncer,
    private dialog: MatDialog) { }

  ngOnInit(): void {
    const dataInicial = new Date();
    dataInicial.setDate(dataInicial.getDate() - 7);
    this.minDate = dataInicial.toISOString().substring(0, 10);
    this.maxDate = new Date().toISOString().substring(0, 10);
    this.listarTodosRomaneios();
  }

  ngAfterViewInit() { }

  announceSortChange(sortState: Sort) {
    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  };

  listarTodosRomaneios() {
    this.service.findAll(this.minDate, this.maxDate).subscribe((response) => {
      this.ELEMENT_DATA = response;
      this.dataSource = new MatTableDataSource<RomaneioGet>(response);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  };

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  };

  openModalView(romaneio) {
    this.dialog.open(RomaneioModal, { data: romaneio });
    const card = document.querySelector('.content');
    card.scrollTop = 0;
  };

  openModalProcessar(romaneio) {
    const dialogRef = this.dialog.open(RomaneioProcessarModal, { data: romaneio });
    const card = document.querySelector('.content');
    card.scrollTop = 0;
    dialogRef.componentInstance.romaneio.subscribe(() => {
      this.listarTodosRomaneios();
    });
  };

  orderByStatus(status: any): void {
    let list: RomaneioGet[] = [];
    this.ELEMENT_DATA.forEach(element => {
      if (element.statusRomaneio == status)
        list.push(element)
    });

    this.recarregaLista(list);
  }

  pesquisaAvancada() {
    let list: RomaneioGet[] = [];
    this.ELEMENT_DATA.forEach(element => {
      if (element.numeroRomaneio == this.numeroRomaneio) {
        list.push(element);
      }
    })

    this.recarregaLista(list);
  }

  listarTodos() {
    this.recarregaLista(this.ELEMENT_DATA);
  }

  retornaStatus(status: Number): String {
    if (status === 0) {
      return "Aberto";
    } else if (status === 1) {
      return "Processado";
    } else if (status === 2) {
      return "Em trânsito";
    } else {
      return "Fechado";
    }
  };

  private recarregaLista(list: any) {
    this.FILTERED_DATA = list;
    this.dataSource = new MatTableDataSource<RomaneioGet>(list);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
}
