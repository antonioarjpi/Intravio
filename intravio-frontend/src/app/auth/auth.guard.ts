import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { UsuariosService } from '../services/usuarios.service';


@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: UsuariosService, private router: Router){}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
    let authenticated = this.authService.isAutenticado();
    if (authenticated){
      return true;
    }else{
      this.router.navigate(['login']);
      return false;
    }
  }
}
