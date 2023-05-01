import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { API_CONFIG } from '../config/api.config';
import { Observable } from 'rxjs';
import { Filial } from '../models/filial';

@Injectable({
  providedIn: 'root'
})
export class FilialService {

  jwtService: JwtHelperService = new JwtHelperService();

  constructor(private http: HttpClient) {}

  findAll(): Observable<Filial[]>{
    return this.http.get<Filial[]>(`${API_CONFIG.baseUrl}/filiais`)
  }

  findById(id: any): Observable<Filial>{
    return this.http.get<Filial>(`${API_CONFIG.baseUrl}/filiais/${id}`)
  }

  create(filial: Filial): Observable<Filial> {
    return this.http.post<Filial>(`${API_CONFIG.baseUrl}/filiais`, filial)
  }

  update(filial: Filial): Observable<Filial> {
    return this.http.put<Filial>(`${API_CONFIG.baseUrl}/filiais/${filial.id}`, filial)
  }

  delete(id: any): Observable<Filial>{
    return this.http.delete<Filial>(`${API_CONFIG.baseUrl}/filiais/${id}`)
  }
}
