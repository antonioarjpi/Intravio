import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Funcionario } from 'src/app/models/funcionario';
import { FuncionarioService } from 'src/app/services/funcionario.service';
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { MatSort, Sort } from '@angular/material/sort';

@Component({
  selector: 'app-funcionario-listar',
  templateUrl: './funcionario-listar.component.html',
  styleUrls: ['./funcionario-listar.component.css']
})
export class FuncionarioListarComponent implements OnInit {
  ELEMENT_DATA: Funcionario[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  displayedColumns: string[] = ["nome", "email", "departamento", "filial", "acoes"];
  dataSource = new MatTableDataSource<Funcionario>(this.ELEMENT_DATA);
  abrePesquisa: boolean = false;

  constructor(
    private service: FuncionarioService,
    private _liveAnnouncer: LiveAnnouncer) { }

  ngOnInit(): void {
    this.listarTodosFuncionarios();
  }

  announceSortChange(sortState: Sort) {
    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  }

  listarTodosFuncionarios() {
    this.service.findAll().subscribe((response) => {
      this.ELEMENT_DATA = response;
      this.dataSource = new MatTableDataSource<Funcionario>(response);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}

