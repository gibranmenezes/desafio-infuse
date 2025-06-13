import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';
import { CreditoService } from './credito.service';

@Injectable({
  providedIn: 'root'
})
export class ConsultaHandlerService {
  constructor(
    private creditoService: CreditoService,
    private snackBar: MatSnackBar
  ) {}

  consultarPorNfse(numeroNfse: string): Observable<any> {
    return this.creditoService.getCreditosByNfse(numeroNfse);
  }

  consultarPorNumeroCredito(numeroCredito: string): Observable<any> {
    return this.creditoService.getCreditoByNumero(numeroCredito);
  }

  processarResposta(response: any, mensagemVazio: string): any {
    if (!response) {
      this.mostrarMensagem('Nenhum dado recebido do servidor');
      return null;
    }
    
    if (response.status === 204 || !response.content || 
        (Array.isArray(response.content) && response.content.length === 0)) {
      this.mostrarMensagem(mensagemVazio);
      return null;
    }
    
    return response.content;
  }

  tratarErro(erro: any, mensagem: string): void {
    this.mostrarMensagem(mensagem);
    console.error(`${mensagem}:`, erro);
  }

  mostrarMensagem(mensagem: string): void {
    this.snackBar.open(mensagem, 'Fechar', {
      duration: 3000
    });
  }
}