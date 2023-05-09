import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import { API_CONFIG } from '../config/api.config';
import {RomaneioFechamento, RomaneioGet, RomaneioInput } from '../models/romaneio';

@Injectable({
  providedIn: 'root'
})
export class RomaneioService {

  jwtService: JwtHelperService = new JwtHelperService();

  constructor(private http: HttpClient) { }

  findAll(): Observable<RomaneioGet[]> {
    return this.http.get<RomaneioGet[]>(`${API_CONFIG.baseUrl}/romaneios`)
  }

  findById(id: any): Observable<RomaneioInput> {
    return this.http.get<RomaneioInput>(`${API_CONFIG.baseUrl}/romaneios/${id}`)
  }

  create(romaneio: RomaneioInput): Observable<RomaneioInput> {
    return this.http.post<RomaneioInput>(`${API_CONFIG.baseUrl}/romaneios`, romaneio)
  }

  update(romaneio: RomaneioInput): Observable<RomaneioInput> {
    return this.http.put<RomaneioInput>(`${API_CONFIG.baseUrl}/romaneios/${romaneio.id}`, romaneio)
  }

  fecharRomaneio(romaneio: RomaneioFechamento): Observable<RomaneioFechamento> {
    return this.http.put<RomaneioFechamento>(`${API_CONFIG.baseUrl}/romaneios/${romaneio.romaneioId}/fechamento`, romaneio)
  }

  processarRomaneio(id: any){
    return this.http.put(`${API_CONFIG.baseUrl}/romaneios/${id}/processar`, null)
  }

  delete(romaneio: any): Observable<void> {
    return this.http.delete<void>(`${API_CONFIG.baseUrl}/romaneios/${romaneio.id}`)
  }

}