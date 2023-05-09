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

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  jwtService: JwtHelperService = new JwtHelperService();

  constructor() { }

  pathnames = [] = ["/rastreamento", "/login"];

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let token = localStorage.getItem('@token');

    if (token) {
      if (this.jwtService.isTokenExpired(token)) {
        if (this.pathnames.includes(window.location.pathname)) {
          return next.handle(request);
        }
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
