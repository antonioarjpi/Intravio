import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HTTP_INTERCEPTORS
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import { ToastrService } from 'ngx-toastr';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  jwtService: JwtHelperService = new JwtHelperService();

  constructor(
    private toast: ToastrService
  ) { }

  pathnames = [] = ["/rastreamento", "/login"];

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let token = localStorage.getItem('@token');

    if (token) {
      if (this.jwtService.isTokenExpired(token)) {
        if (this.pathnames.includes(window.location.pathname)) {
          return next.handle(request);
        }
        this.toast.error("Sessão expirada! Por favor, faça login novamente.")
        localStorage.clear();
      }
      const cloneReq =
        request.clone({ headers: request.headers.set('Authorization', `Bearer ${token}`) });
      return next.handle(cloneReq);
    } else {
      return next.handle(request);
    }
  }
}

export const AuthInterceptorProvider = [
  {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true
  }
]
