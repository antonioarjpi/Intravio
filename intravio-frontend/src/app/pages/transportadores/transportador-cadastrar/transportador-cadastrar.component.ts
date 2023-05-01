import { Component } from "@angular/core";
import { UntypedFormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Transportador } from 'src/app/models/transportador';
import { TransportadorService } from 'src/app/services/transportador.service';

@Component({
  selector: 'app-transportador-cadastrar',
  templateUrl: './transportador-cadastrar.component.html',
  styleUrls: ['./transportador-cadastrar.component.css']
})
export class TransportadorCadastrarComponent {

  nome: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));
  motorista: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));
  placa: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(8));
  veiculo: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));

  transportador: Transportador = {
    id: "",
    nome: "",
    motorista: "",
    placa: "",
    veiculo: "",
    observacao: "",
    cnpj: ""
  };

  constructor(
    private service: TransportadorService,
    private toast: ToastrService,
    private router: Router
  ) { };

  cadastrarTransportador(): void {
    if (!this.validaCampos()) {
      return;
    }

    this.service.create(this.transportador).subscribe(
      () => {
        this.toast.success("Transportador cadastrado com sucesso", "Cadastro");
        this.router.navigate(["transportadores"])
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
      this.nome.valid && this.motorista.valid && this.placa.valid && this.veiculo.valid
    );
  }

  converteParaMaiusculo(event) {
    this.transportador.placa = event.target.value.toUpperCase();
  }
}
