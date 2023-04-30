import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { JwtHelperService } from "@auth0/angular-jwt";

import { Credentials } from "./../models/credentials";
import { API_CONFIG } from "src/app/config/api.config";

@Injectable({
  providedIn: 'root'
})
export class UsuariosService {

  jwtService: JwtHelperService = new JwtHelperService();

  constructor(private http: HttpClient) {}

  autenticar(creds: Credentials) {
    return this.http.post(`${API_CONFIG.baseUrl}/usuarios/autenticar`, creds, {
      observe: 'response',
      responseType: 'text'
    })
  }

  sucessoLogin(authToken: string){
    localStorage.setItem('@token', authToken)
  }

  isAutenticado() {
    let token = localStorage.getItem("@token")
    if (token != null){
      return !this.jwtService.isTokenExpired(token)
    }
    return false;
  }

  logoff(){
    localStorage.clear();
  }
}
