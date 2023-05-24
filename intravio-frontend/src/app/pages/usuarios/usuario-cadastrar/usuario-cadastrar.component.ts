import { Component } from '@angular/core';
import { UntypedFormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Departamento } from 'src/app/models/departamento';
import { Filial } from 'src/app/models/filial';
import { Usuario } from 'src/app/models/usuario';
import { DepartamentoService } from 'src/app/services/departamento.service';
import { FilialService } from 'src/app/services/filial.service';
import { UsuarioService } from 'src/app/services/usuarios.service';

@Component({
  selector: 'app-usuario-cadastrar',
  templateUrl: './usuario-cadastrar.component.html',
  styleUrls: ['./usuario-cadastrar.component.css']
})
export class UsuarioCadastrarComponent {
  primeiroNome: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));
  segundoNome: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(1));
  perfil: UntypedFormControl = new UntypedFormControl(null, Validators.nullValidator);
  email: UntypedFormControl = new UntypedFormControl(null, Validators.nullValidator);
  senha: UntypedFormControl = new UntypedFormControl(null, Validators.minLength(3));
  senhaRepetida: string = "";

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

  cadastrarUsuario(): void {
    console.log("senha: " + this.senhaRepetida)
    if (!this.validaCampos()) {
      return;
    }

    this.service.create(this.usuario).subscribe(
      () => {
        this.toast.success("Usuário cadastrado com sucesso", "Cadastro");
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
    if (this.senhaRepetida !== this.usuario.senha) {
      this.toast.error("As senhas digitadas não correspondem. Por favor, verifique se você digitou as duas senhas exatamente iguais.", "Erro")
      return false;
    }
    return (
      this.primeiroNome.valid && this.email.valid && this.segundoNome.valid && this.perfil.valid && this.senha.valid
    );
  }

}
