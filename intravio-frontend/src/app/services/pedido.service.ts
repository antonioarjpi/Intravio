import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import { API_CONFIG } from '../config/api.config';
import { Pedido, PedidoInput } from '../models/pedido';

@Injectable({
  providedIn: 'root'
})
export class PedidoService {

  jwtService: JwtHelperService = new JwtHelperService();

  constructor(private http: HttpClient) { }

  findAll(): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(`${API_CONFIG.baseUrl}/pedidos`)
  }

  findAllByStatus(status: any): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(`${API_CONFIG.baseUrl}/pedidos/status?status=${status}`)
  }


  findById(id: any): Observable<PedidoInput> {
    return this.http.get<PedidoInput>(`${API_CONFIG.baseUrl}/pedidos/${id}`)
  }

  create(pedido: PedidoInput): Observable<PedidoInput> {
    return this.http.post<PedidoInput>(`${API_CONFIG.baseUrl}/pedidos`, pedido)
  }

  update(pedido: PedidoInput): Observable<PedidoInput> {
    return this.http.put<PedidoInput>(`${API_CONFIG.baseUrl}/pedidos/${pedido.id}`, pedido)
  }

  delete(id: any, motivo: string): Observable<void> {
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      body: { motivo: motivo }
    };
    return this.http.delete<void>(`${API_CONFIG.baseUrl}/pedidos/${id}`, options)
  }

  uploadFiles(id: any, arquivos: File[]) {
    const formData = new FormData();
    for (var i = 0; i < arquivos.length; i++) {
      formData.append('files', arquivos[i]);

    }
    return this.http.post(`${API_CONFIG.baseUrl}/pedidos/${id}/imagem/adicionar`, formData);
  };

  exibirImagem(filename: string) {
    return this.http.get(`${API_CONFIG.baseUrl}/pedidos/${filename}/imagens`, { responseType: 'blob' })
  }

  baixarImagens(filename: string) {
    return this.http.get(`${API_CONFIG.baseUrl}/pedidos/${filename}/download/imagens`, { responseType: 'blob' })
  }

}