import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UsuarioService } from 'src/app/services/usuarios.service';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {

  expandCadastro: boolean = false;
  expandConfig: boolean = false;
  isAdmin: boolean = this.usuarioService.getRoles().includes('ADMIN');

  constructor(private router: Router,
    private usuarioService: UsuarioService) { }

  ngOnInit(): void {
  }

  toogleCadastro() {
    if (this.expandCadastro) {
      this.expandCadastro = false;
    } else if (!this.expandCadastro) {
      this.expandCadastro = true;
      this.expandConfig = false
    }
  }

  toogleConfig() {
    if (this.expandConfig) {
      this.expandConfig = false;
    } else if (!this.expandConfig) {
      this.expandConfig = true;
      this.expandCadastro = false
    }
  }

  desabilita(){
    this.expandConfig = false;
    this.expandCadastro = false;
  }

  logout() {
    this.usuarioService.logoff();
    this.router.navigate(['login'])
  }
}
