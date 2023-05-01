import { Component } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Departamento } from 'src/app/models/departamento';
import { DepartamentoService } from 'src/app/services/departamento.service';

@Component({
  selector: 'app-cadastrar-departamento',
  templateUrl: './cadastrar-departamento.component.html',
  styleUrls: ['./cadastrar-departamento.component.css']
})
export class CadastrarDepartamentoComponent {

  nome: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));

  departamento: Departamento = {
    id: "",
    nome: ""
  };

  constructor(
    private service: DepartamentoService,
    private toast: ToastrService,
    private router: Router
  ) { };

  cadastrarDepartamento(): void {
    if (!this.validaCampos()) {
      return;
    }

    this.service.create(this.departamento).subscribe(
      () => {
        this.toast.success("Departamento cadastrado com sucesso", "Cadastro");
        this.router.navigate(["departamentos"])
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
      this.nome.valid
    );
  }
}
