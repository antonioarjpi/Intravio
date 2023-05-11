import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Filial } from 'src/app/models/filial';
import { FilialService } from 'src/app/services/filial.service';

@Component({
  selector: 'app-filial-listar',
  templateUrl: './filial-listar.component.html',
  styleUrls: ['./filial-listar.component.css']
})
export class FilialListarComponent implements OnInit {
  ELEMENT_DATA: Filial[] = [];
  @ViewChild(MatPaginator) paginator: MatPaginator;

  displayedColumns: string[] = ["id","nome", "endereco", "complemento", "acoes"];
  dataSource = new MatTableDataSource<Filial>(this.ELEMENT_DATA);

  constructor(private service: FilialService) { }

  ngOnInit(): void {
    this.listarTodosDepartamentos();
  }

  ngAfterViewInit() { }

  listarTodosDepartamentos() {
    this.service.findAll().subscribe((response) => {
      this.ELEMENT_DATA = response;
      this.dataSource = new MatTableDataSource<Filial>(response);
      this.dataSource.paginator = this.paginator;
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}
