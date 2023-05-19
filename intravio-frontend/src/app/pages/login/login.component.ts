import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, Validators } from "@angular/forms";
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Credentials } from 'src/app/models/credentials';
import { UsuarioService } from 'src/app/services/usuarios.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  creds: Credentials = {
    email: "",
    senha: "",
  };
  constructor(
    private toast: ToastrService,
    private service: UsuarioService,
    private router: Router
  ) {}

  email = new UntypedFormControl(null, Validators.email);
  senha = new UntypedFormControl(null, Validators.minLength(6));

  ngOnInit(): void { }

  login() {
    localStorage.clear();
    this.service.autenticar(this.creds).subscribe((response) => {
        this.service.sucessoLogin(
          response.headers.get("Authorization").substring(7));
        this.router.navigate(['home']);
      },
      () => {
        this.toast.error("Usuário ou/e senha inválidos.", "Login");
      }
    );
  }

  validateFields(): boolean {
    if (this.email.valid && this.senha.valid) {
      return true;
    }
    return false;
  }
}
