import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import { API_CONFIG } from '../config/api.config';
import {RomaneioGet, RomaneioInput } from '../models/romaneio';

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

  delete(id: any, motivo: string): Observable<void> {
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      body: { motivo: motivo }
    };
    return this.http.delete<void>(`${API_CONFIG.baseUrl}/romaneios/${id}`, options)
  }

}