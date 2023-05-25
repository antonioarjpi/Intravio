import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Departamento } from 'src/app/models/departamento';
import { Filial } from 'src/app/models/filial';
import { Funcionario } from 'src/app/models/funcionario';
import { DepartamentoService } from 'src/app/services/departamento.service';
import { FilialService } from 'src/app/services/filial.service';
import { FuncionarioService } from 'src/app/services/funcionario.service';

@Component({
  selector: 'app-funcionario-cadastrar',
  templateUrl: './funcionario-cadastrar.component.html',
  styleUrls: ['./funcionario-cadastrar.component.css']
})
export class FuncionarioCadastrarComponent implements OnInit {

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
    private router: Router
  ) { };

  ngOnInit(): void {
    this.departamentoService.findAll().subscribe((response) => {
      this._departamento = response
    });

    this.filialService.findAll().subscribe((response) => {
      this._filial = response
    })
  };

  cadastrarFuncionario(): void {

    if (!this.validaCampos()) {
      return;
    }

    this.service.create(this.funcionario).subscribe(() => {
      this.toast.success("Usuário cadastrado com sucesso", "Cadastro");
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

  validaCampos(): boolean {
    return (
      this.nome.valid && this.email.valid
    );
  }
}

