import { Component } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Filial } from 'src/app/models/filial';
import { FilialService } from 'src/app/services/filial.service';

@Component({
  selector: 'app-filial-cadastrar',
  templateUrl: './filial-cadastrar.component.html',
  styleUrls: ['./filial-cadastrar.component.css']
})
export class FilialCadastrarComponent {

  id: UntypedFormControl = new UntypedFormControl(null, Validators.nullValidator);
  nome: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));
  rua: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));
  numero: UntypedFormControl = new UntypedFormControl(null, Validators.nullValidator);
  bairro: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));
  cep: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));
  cidade: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));
  estado: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));

  filial: Filial = {
    id: null,
    nome: "",
    rua: "",
    cep: "",
    bairro: "",
    numero: null,
    complemento: "",
    estado: "",
    cidade: "",
  };

  constructor(
    private service: FilialService,
    private toast: ToastrService,
    private router: Router
  ) { };

  cadastrarFilial(): void {
    
    if (!this.validaCampos()) {
      return;
    }

    this.service.create(this.filial).subscribe(
      () => {
        this.toast.success("Filial cadastrada com sucesso", "Cadastro");
        this.router.navigate(["filiais"])
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
      this.nome.valid && this.id.valid && this.cidade.valid && this.rua.valid
      && this.estado.valid && this.numero.valid && this.bairro.valid && this.cep.valid
    );
  }
}

