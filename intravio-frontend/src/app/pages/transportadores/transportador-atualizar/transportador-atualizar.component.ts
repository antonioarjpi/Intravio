import { Component, OnInit } from "@angular/core";
import { UntypedFormControl, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Transportador } from "src/app/models/transportador";
import { TransportadorService } from 'src/app/services/transportador.service';

@Component({
  selector: 'app-transportador-atualizar',
  templateUrl: './transportador-atualizar.component.html',
  styleUrls: ['./transportador-atualizar.component.css']
})
export class TransportadorAtualizarComponent implements OnInit {

  nome: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));
  motorista: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));
  placa: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(7));
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
    private router: Router,
    private route: ActivatedRoute
  ) { };


  ngOnInit(): void {
    this.transportador.id = this.route.snapshot.paramMap.get('id');
    this.buscarPorId();
  }


  buscarPorId(): void {
    this.service.findById(this.transportador.id).subscribe(response => {
      this.transportador = response
    })
  }

  atualizarTransportador(): void {
    if (!this.validaCampos()) {
      return;
    }

    this.service.update(this.transportador).subscribe(
      () => {
        this.toast.success("Transportador atualizado com sucesso", "Atualização");
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
      this.nome.valid
    );
  }

  converteParaMaiusculo(event) {
    this.transportador.placa = event.target.value.toUpperCase();
  }
}