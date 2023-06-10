import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Departamento } from 'src/app/models/departamento';
import { Filial } from 'src/app/models/filial';
import { Funcionario } from 'src/app/models/funcionario';
import { DepartamentoService } from 'src/app/services/departamento.service';
import { FilialService } from 'src/app/services/filial.service';
import { FuncionarioService } from 'src/app/services/funcionario.service';

@Component({
  selector: 'app-funcionario-atualizar',
  templateUrl: './funcionario-atualizar.component.html',
  styleUrls: ['./funcionario-atualizar.component.css']
})
export class FuncionarioAtualizarComponent implements OnInit {

  nome: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(3));
  email: UntypedFormControl = new UntypedFormControl(null, Validators.email);

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

    this.departamentoService.findAll().subscribe((response) => {
      this._departamento = response
    });

    this.filialService.findAll().subscribe((response) => {
      this._filial = response
    })

    this.buscarPorId();

  };

  buscarPorId(): void {
    this.service.findById(this.funcionario.id).subscribe((response) => {
      this.funcionario = response;
      this.funcionario.departamento = this._departamento.find(obj => obj.nome === response.departamento).id //Busca o departamento que veio na resposta e insere no funcionário
      this.funcionario.filial = this._filial.find(obj => obj.nome === response.filial.toString()).id; //Busca a Filial que veio na resposta e insere no funcionário
    })
  }

  atualizarFuncionario(): void {

    if (!this.validaCampos()) {
      return;
    }

    this.service.update(this.funcionario).subscribe(
      () => {
        this.toast.success("Usuário atualizado com sucesso", "Atualização");
        this.router.navigate(["usuarios"])
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

  validaCampos(): boolean {
    return (
      this.nome.valid && this.email.valid
    );
  }
}
