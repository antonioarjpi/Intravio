import { Component, OnInit, ViewChild } from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { MatTableDataSource } from "@angular/material/table";
import { Departamento } from 'src/app/models/departamento';
import { DepartamentoService } from "src/app/services/departamento.service";

@Component({
  selector: 'app-listar-departamento',
  templateUrl: './listar-departamento.component.html',
  styleUrls: ['./listar-departamento.component.css']
})
export class ListarDepartamentoComponent implements OnInit {
  ELEMENT_DATA: Departamento[] = [];
  @ViewChild(MatPaginator) paginator: MatPaginator;

  displayedColumns: string[] = ["nome", "acoes"];
  dataSource = new MatTableDataSource<Departamento>(this.ELEMENT_DATA);

  constructor(private service: DepartamentoService) {}

  ngOnInit(): void {
    this.listarTodosDepartamentos();
  }

  ngAfterViewInit() {}

  listarTodosDepartamentos() {
    this.service.findAll().subscribe((response) => {
      this.ELEMENT_DATA = response;
      this.dataSource = new MatTableDataSource<Departamento>(response);
      this.dataSource.paginator = this.paginator;
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}
