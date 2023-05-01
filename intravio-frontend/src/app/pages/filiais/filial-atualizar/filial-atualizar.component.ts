import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Filial } from 'src/app/models/filial';
import { FilialService } from 'src/app/services/filial.service';

@Component({
  selector: 'app-filial-atualizar',
  templateUrl: './filial-atualizar.component.html',
  styleUrls: ['./filial-atualizar.component.css']
})
export class FilialAtualizarComponent implements OnInit {

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
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.filial.id = Number(this.route.snapshot.paramMap.get('id'));
    this.buscaPorId();
  };

  buscaPorId(): void {
    this.service.findById(this.filial.id).subscribe((response) => {
      this.filial = response;
    })
  }

  atualizarFilial(): void {

    if (!this.validaCampos()) {
      return;
    }

    this.service.create(this.filial).subscribe(
      () => {
        this.toast.success("Filial atualizada com sucesso", "Atualização");
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