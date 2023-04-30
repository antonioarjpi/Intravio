import { Component, OnInit } from "@angular/core";
import { UntypedFormControl, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Departamento } from 'src/app/models/departamento';
import { DepartamentoService } from 'src/app/services/departamento.service';

@Component({
  selector: 'app-atualizar-departamento',
  templateUrl: './atualizar-departamento.component.html',
  styleUrls: ['./atualizar-departamento.component.css']
})
export class AtualizarDepartamentoComponent implements OnInit {

  nome: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));

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
    this.service.findById(this.departamento.id).subscribe(response => {
      this.departamento = response
    })
  }

  atualizarDepartamento(): void {
    if (!this.validaCampos()) {
      return;
    }

    this.service.update(this.departamento).subscribe(
      () => {
        this.toast.success("Departamento atualizado com sucesso", "Atualização");
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
