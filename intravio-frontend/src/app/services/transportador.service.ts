import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { API_CONFIG } from '../config/api.config';
import { Observable } from 'rxjs';
import { Transportador } from '../models/transportador';

@Injectable({
  providedIn: 'root'
})
export class TransportadorService {

  jwtService: JwtHelperService = new JwtHelperService();

  constructor(private http: HttpClient) { }

  findAll(): Observable<Transportador[]> {
    return this.http.get<Transportador[]>(`${API_CONFIG.baseUrl}/transportadores`)
  }

  findById(id: any): Observable<Transportador> {
    return this.http.get<Transportador>(`${API_CONFIG.baseUrl}/transportadores/${id}`)
  }

  create(transportador: Transportador): Observable<Transportador> {
    return this.http.post<Transportador>(`${API_CONFIG.baseUrl}/transportadores`, transportador)
  }

  update(transportador: Transportador): Observable<Transportador> {
    return this.http.put<Transportador>(`${API_CONFIG.baseUrl}/transportadores/${transportador.id}`, transportador)
  }

  delete(id: any): Observable<Transportador> {
    return this.http.delete<Transportador>(`${API_CONFIG.baseUrl}/transportadores/${id}`)
  }
}
