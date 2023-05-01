import { Component, OnInit, ViewChild } from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { MatTableDataSource } from "@angular/material/table";
import { Transportador } from 'src/app/models/transportador';
import { TransportadorService } from "src/app/services/transportador.service";

@Component({
  selector: 'app-transportador-listar',
  templateUrl: './transportador-listar.component.html',
  styleUrls: ['./transportador-listar.component.css']
})
export class TransportadorListarComponent implements OnInit {
  ELEMENT_DATA: Transportador[] = [];
  @ViewChild(MatPaginator) paginator: MatPaginator;

  displayedColumns: string[] = ["nome", "veiculo", "placa", "motorista", "cnpj", "observacao", "acoes"];
  dataSource = new MatTableDataSource<Transportador>(this.ELEMENT_DATA);

  constructor(private service: TransportadorService) {}

  ngOnInit(): void {
    this.listarTodosTransportadors();
  }

  ngAfterViewInit() {}

  listarTodosTransportadors() {
    this.service.findAll().subscribe((response) => {
      this.ELEMENT_DATA = response;
      this.dataSource = new MatTableDataSource<Transportador>(response);
      this.dataSource.paginator = this.paginator;
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}
