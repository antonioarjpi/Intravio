import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Departamento } from 'src/app/models/departamento';
import { Filial } from 'src/app/models/filial';
import { Funcionario } from 'src/app/models/funcionario';
import { DepartamentoService } from 'src/app/services/departamento.service';
import { FilialService } from 'src/app/services/filial.service';
import { FuncionarioService } from 'src/app/services/funcionario.service';

@Component({
  selector: 'app-funcionario-deletar',
  templateUrl: './funcionario-deletar.component.html',
  styleUrls: ['./funcionario-deletar.component.css']
})
export class FuncionarioDeletarComponent implements OnInit {

  funcionario: Funcionario = {
    id: null,
    nome: "",
    email: "",
    departamento: null,
    filial: null
  };

  _filial: Filial[] = [];
  _departamento: Departamento[] = [];

  constructor(
    private service: FuncionarioService,
    private departamentoService: DepartamentoService,
    private filialService: FilialService,
    private toast: ToastrService,
    private router: Router,
    private route: ActivatedRoute
  ) { };

  ngOnInit(): void {
    this.funcionario.id = this.route.snapshot.paramMap.get("id");

    this.buscarPorId();

    this.departamentoService.findAll().subscribe((response) => {
      this._departamento = response
    });

    this.filialService.findAll().subscribe((response) => {
      this._filial = response
    })
  };

  buscarPorId(): void {
    this.service.findById(this.funcionario.id).subscribe((response) => {
      this.funcionario = response;
    })
  }

  deletarFuncionario(): void {
    this.service.delete(this.funcionario.id).subscribe(() => {
      this.toast.success("Usuário deletado com sucesso", "Exclusão");
      this.router.navigate(["usuarios"]);
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
  }
}