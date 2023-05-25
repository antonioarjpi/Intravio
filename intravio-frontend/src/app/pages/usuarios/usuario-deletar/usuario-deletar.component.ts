import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Usuario } from 'src/app/models/usuario';
import { UsuarioService } from 'src/app/services/usuarios.service';

@Component({
  selector: 'app-usuario-deletar',
  templateUrl: './usuario-deletar.component.html',
  styleUrls: ['./usuario-deletar.component.css']
})
export class UsuarioDeletarComponent {

  id: string;
  usuario: Usuario = {
    id: null,
    primeiroNome: "",
    segundoNome: "",
    email: "",
    senha: "",
    perfil: "STANDARD",
  };

  constructor(
    private service: UsuarioService,
    private toast: ToastrService,
    private route: ActivatedRoute,
    private router: Router
  ) { };

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id')

    this.buscaUsuarioPorId();
  };

  buscaUsuarioPorId() {
    this.service.findById(this.id).subscribe((response) => {
      this.usuario = response;
    }), (ex) => {
      this.toast.error(ex.error.message)
    }
  }

  deletarUsuario(): void {
    this.service.delete(this.usuario).subscribe(() => {
      this.toast.success("Usuário deletado com sucesso", "Exclusão");
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
}