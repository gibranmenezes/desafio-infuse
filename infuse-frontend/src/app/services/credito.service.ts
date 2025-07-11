import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, catchError, tap } from 'rxjs';
import { Credito } from '../models/credito.model';
import { AppResponse } from '../models/app-response.model';
import { API_BASE_URL } from '../app.constants';

@Injectable({
  providedIn: 'root'
})
export class CreditoService {
  private http = inject(HttpClient);
  private apiUrl = `${API_BASE_URL}/creditos`;

  getCreditosByNfse(numeroNfse: string): Observable<AppResponse<Credito[]>> {
    return this.http.get<AppResponse<Credito[]>>(`${this.apiUrl}/${numeroNfse}`)
      .pipe(
        tap(response => console.log('Resposta da API (NFSe):', response)),
        catchError(error => {
          console.error('Erro ao buscar créditos por NFSe:', error);
          return of({
            status: error.status || 204,
            message: error.error?.message || 'Erro ao buscar créditos',
            content: []
          });
        })
      );
  }

  getCreditoByNumero(numeroCredito: string): Observable<AppResponse<Credito>> {
    return this.http.get<AppResponse<Credito>>(`${this.apiUrl}/credito/${numeroCredito}`)
      .pipe(
        tap(response => console.log('Resposta da API (Crédito):', response)),
        catchError(error => {
          console.error('Erro ao buscar crédito por número:', error);
          return of({
            status: error.status || 204,
            message: error.error?.message || 'Erro ao buscar crédito',
            content: null as unknown as Credito
          });
        })
      );
  }
}