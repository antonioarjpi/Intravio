import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Produto } from '../models/produto';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../config/api.config';

@Injectable({
  providedIn: 'root'
})
export class ProdutoService {

  jwtService: JwtHelperService = new JwtHelperService();

  constructor(private http: HttpClient) { }

  findAll(): Observable<Produto[]> {
    return this.http.get<Produto[]>(`${API_CONFIG.baseUrl}/produtos`)
  }

  findById(id: any): Observable<Produto> {
    return this.http.get<Produto>(`${API_CONFIG.baseUrl}/produtos/${id}`)
  }

  create(produto: Produto): Observable<Produto> {
    return this.http.post<Produto>(`${API_CONFIG.baseUrl}/produtos`, produto)
  }

  update(produto: Produto): Observable<Produto> {
    return this.http.put<Produto>(`${API_CONFIG.baseUrl}/produtos/${produto.id}`, produto)
  }

  delete(id: any): Observable<Produto> {
    return this.http.delete<Produto>(`${API_CONFIG.baseUrl}/produtos/${id}`)
  }
}
