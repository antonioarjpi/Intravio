import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { UsuarioService } from '../services/usuarios.service';
import { ToastrService } from 'ngx-toastr';


@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private toast: ToastrService,
    private authService: UsuarioService,
    private router: Router
  ) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
    let authenticated = this.authService.isAutenticado();
    const requiredRole = route.data['role'] as string[];
    if (authenticated && this.authService.hasAnyRole(requiredRole)) {
      return true;
    }
    else if (authenticated) {
      this.toast.warning("Você não possui permissão para acessar esta página", "Acesso Negado")
      this.router.navigate(['home']);

      return true;
    } else {
      this.router.navigate(['login']);
      return false;
    }
  }
}
