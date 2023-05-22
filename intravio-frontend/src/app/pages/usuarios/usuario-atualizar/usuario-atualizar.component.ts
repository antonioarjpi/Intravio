import { Component } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Departamento } from 'src/app/models/departamento';
import { Filial } from 'src/app/models/filial';
import { Usuario } from 'src/app/models/usuario';
import { DepartamentoService } from 'src/app/services/departamento.service';
import { FilialService } from 'src/app/services/filial.service';
import { UsuarioService } from 'src/app/services/usuarios.service';

@Component({
  selector: 'app-usuario-atualizar',
  templateUrl: './usuario-atualizar.component.html',
  styleUrls: ['./usuario-atualizar.component.css']
})
export class UsuarioAtualizarComponent {

  id: string;
  primeiroNome: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(3));
  segundoNome: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(3));
  perfil: UntypedFormControl = new UntypedFormControl(null, Validators.nullValidator);
  email: UntypedFormControl = new UntypedFormControl(null, Validators.email);

  usuario: Usuario = {
    id: null,
    primeiroNome: "",
    segundoNome: "",
    email: "",
    senha: "",
    perfil: "STANDARD",
  };

  _filial: Filial[] = [];
  _departamento: Departamento[] = [];

  constructor(
    private service: UsuarioService,
    private departamentoService: DepartamentoService,
    private filialService: FilialService,
    private toast: ToastrService,
    private route: ActivatedRoute,
    private router: Router
  ) { };

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id')

    this.departamentoService.findAll().subscribe((response) => {
      this._departamento = response
    });

    this.filialService.findAll().subscribe((response) => {
      this._filial = response
    });

    this.buscaUsuarioPorId();
  };

  buscaUsuarioPorId() {
    this.service.findById(this.id).subscribe(response => {
      this.usuario = response;
    }), (ex) => {
      this.toast.error(ex.error.message)
    }
  }

  atualizarUsuario(): void {
    if (!this.validaCampos()) {
      return;
    }

    this.service.update(this.usuario).subscribe(
      () => {
        this.toast.success("Usuário atualizado com sucesso", "Atualização");
        this.router.navigate(["/sistema/usuarios"])
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
      this.primeiroNome.valid && this.email.valid && this.segundoNome.valid && this.perfil.valid
    );
  }

}
