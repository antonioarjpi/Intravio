import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { JwtHelperService } from "@auth0/angular-jwt";
import { Observable } from 'rxjs';
import { API_CONFIG } from "src/app/config/api.config";
import { Departamento } from "../models/departamento";

@Injectable({
  providedIn: 'root'
})
export class DepartamentoService {

  jwtService: JwtHelperService = new JwtHelperService();

  constructor(private http: HttpClient) {}

  findAll(): Observable<Departamento[]>{
    return this.http.get<Departamento[]>(`${API_CONFIG.baseUrl}/departamentos`)
  }

  findById(id: any): Observable<Departamento>{
    return this.http.get<Departamento>(`${API_CONFIG.baseUrl}/departamentos/${id}`)
  }

  create(departamento: Departamento): Observable<Departamento> {
    return this.http.post<Departamento>(`${API_CONFIG.baseUrl}/departamentos`, departamento)
  }

  update(departamento: Departamento): Observable<Departamento> {
    return this.http.put<Departamento>(`${API_CONFIG.baseUrl}/departamentos/${departamento.id}`, departamento)
  }

  delete(id: any): Observable<Departamento>{
    return this.http.delete<Departamento>(`${API_CONFIG.baseUrl}/departamentos/${id}`)
  }

}
