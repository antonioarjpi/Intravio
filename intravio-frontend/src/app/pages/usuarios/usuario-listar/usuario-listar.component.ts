import { Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { MatSort, Sort } from '@angular/material/sort';
import { UsuarioService } from 'src/app/services/usuarios.service';
import { Usuario } from 'src/app/models/usuario';
import { MatDialog } from '@angular/material/dialog';
import { UsuarioAlterarSenhaComponent } from '../usuario-alterar-senha/usuario-alterar-senha.component';

@Component({
  selector: 'app-usuario-listar',
  templateUrl: './usuario-listar.component.html',
  styleUrls: ['./usuario-listar.component.css']
})
export class UsuarioListarComponent {
  ELEMENT_DATA: Usuario[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  displayedColumns: string[] = ["primeiroNome", "segundoNome", "email", "perfil", "acoes"];
  dataSource = new MatTableDataSource<Usuario>(this.ELEMENT_DATA);
  abrePesquisa: boolean = false;

  constructor(
    private service: UsuarioService,
    private _liveAnnouncer: LiveAnnouncer,
    private dialog: MatDialog
  ) { }

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
      this.dataSource = new MatTableDataSource<Usuario>(response);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  openModalView(id) {
    const dialogRef = this.dialog.open(UsuarioAlterarSenhaComponent, { data: id });
    dialogRef.afterClosed().subscribe();
  };
}
