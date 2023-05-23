import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { JwtHelperService } from "@auth0/angular-jwt";

import { Observable } from 'rxjs';
import { Credentials } from "./../models/credentials";
import { API_CONFIG } from "src/app/config/api.config";
import { Usuario } from "../models/usuario";

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  jwtService: JwtHelperService = new JwtHelperService();

  constructor(private http: HttpClient) { }

  autenticar(creds: Credentials) {
    return this.http.post(`${API_CONFIG.baseUrl}/usuarios/autenticar`, creds, {
      observe: 'response',
      responseType: 'text'
    })
  }

  sucessoLogin(authToken: string) {
    localStorage.setItem('@token', authToken)
  }

  findById(id: string): Observable<Usuario> {
    return this.http.get<Usuario>(`${API_CONFIG.baseUrl}/usuarios/${id}`)
  }

  findAll(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${API_CONFIG.baseUrl}/usuarios`)
  }

  create(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(`${API_CONFIG.baseUrl}/usuarios/cadastrar`, usuario)
  }

  update(usuario: Usuario): Observable<Usuario> {
    return this.http.put<Usuario>(`${API_CONFIG.baseUrl}/usuarios/${usuario.id}`, usuario)
  }

  delete(usuario: Usuario): Observable<void> {
    return this.http.delete<void>(`${API_CONFIG.baseUrl}/usuarios/${usuario.id}`)
  }

  isAutenticado() {
    let token = localStorage.getItem("@token");

    if (token != null) {
      return !this.jwtService.isTokenExpired(token)
    }
    return false;
  }

  getRoles():any[] {
    let token = localStorage.getItem("@token");

    let decodedToken = this.decodeToken(token);

    return decodedToken.role
  }

  hasAnyRole(requiredRoles: string[]): boolean {
    const userRoles = this.getRoles();

    return requiredRoles.some(role => userRoles.includes(role));
  }

  decodeToken(token: string) {
    const tokenParts = token.split('.');
    const encodedPayload = tokenParts[1];

    const decodedPayload = atob(encodedPayload);
    const decodedPayloadObject = JSON.parse(decodedPayload);

    return decodedPayloadObject;
  }

  logoff() {
    localStorage.clear();
  }
}
