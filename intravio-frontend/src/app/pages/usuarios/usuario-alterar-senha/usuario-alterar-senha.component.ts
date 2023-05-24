import { Component, Inject, EventEmitter } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { AlterarSenha } from 'src/app/models/alterarSenha';
import { Usuario } from 'src/app/models/usuario';
import { UsuarioService } from 'src/app/services/usuarios.service';
import { UsuarioListarComponent } from '../usuario-listar/usuario-listar.component';

@Component({
  selector: 'app-usuario-alterar-senha',
  templateUrl: './usuario-alterar-senha.component.html',
  styleUrls: ['./usuario-alterar-senha.component.css']
})
export class UsuarioAlterarSenhaComponent {

  vibrar: boolean = false;
  erro: string = '';
  senhaRepetida: string = '';
  senhas: AlterarSenha = {
    senhaAtual: '',
    senhaNova: ''
  }

  ev = new EventEmitter<void>();

  constructor(
    private dialogRef: MatDialogRef<UsuarioListarComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Usuario,
    private toast: ToastrService,
    private service: UsuarioService
  ) { }

  alteraSenha() {
    if (this.senhaRepetida !== this.senhas.senhaNova) {
      this.erro = "Digite uma nova senha e confirme-a corretamente. ";
      this.vibrar = true;
      setTimeout(() => {
        this.vibrar = false;
      }, 500);
      return;
    }

    if (this.senhaRepetida.length < 3 || this.senhas.senhaNova.length < 3) {
      this.erro = "A senha deve ter no mínimo 3 caracteres.";
      this.vibrar = true;
      setTimeout(() => {
        this.vibrar = false;
      }, 500);
      return;
    }

    this.service.changePassword(this.data.id, this.senhas).subscribe(() => {
      this.toast.success("Senha alterada com sucesso", "Alteração de Senha");
      this.dialogRef.close();
    },
      (ex) => {
        this.erro = ex.error.message
        this.vibrar = true;
        setTimeout(() => {
          this.vibrar = false;
        }, 500);
      }
    );
  }

}
