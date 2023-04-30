import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UsuariosService } from 'src/app/services/usuarios.service';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {

  constructor(private router: Router,
    private usuarioService: UsuariosService) { }

  ngOnInit(): void {
  
  }

  logout() {
    this.usuarioService.logoff();
    this.router.navigate(['login'])
  }
}
