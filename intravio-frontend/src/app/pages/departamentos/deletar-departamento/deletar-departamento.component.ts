import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Departamento } from 'src/app/models/departamento';
import { DepartamentoService } from 'src/app/services/departamento.service';

@Component({
  selector: 'app-deletar-departamento',
  templateUrl: './deletar-departamento.component.html',
  styleUrls: ['./deletar-departamento.component.css']
})
export class DeletarDepartamentoComponent implements OnInit {

  departamento: Departamento = {
    id: "",
    nome: ""
  };

  constructor(
    private service: DepartamentoService,
    private toast: ToastrService,
    private router: Router,
    private route: ActivatedRoute
  ) { };

  ngOnInit(): void {
    this.departamento.id = this.route.snapshot.paramMap.get('id');
    this.buscarPorId();
  }

  buscarPorId(): void {
    this.service.findById(this.departamento.id).subscribe((response) => {
      this.departamento = response;
    })
  }

  deletarDepartamento(): void {
    this.service.delete(this.departamento.id).subscribe(
      () => {
        this.toast.success("Departamento deletado com sucesso", "Exclusão");
        this.router.navigate(["departamentos"]);
      },
      (ex) => {
        this.toast.error(ex.error.message);
      }
    );
  }
}
