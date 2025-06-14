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
      return { mensagemErro: 'Nenhum dado recebido do servidor' };
    }
    
    if (response.status === 204 || 
        !response.content || 
        (Array.isArray(response.content) && response.content.length === 0)) {
      return { mensagemErro: mensagemVazio };
    }
    
    if (response.content !== undefined) {
      return response.content;
    } else if (response.data !== undefined) {
      return response.data;
    }
    
    return response;
  }

  tratarErro(erro: any, mensagem: string): any {
    console.error(`${mensagem}:`, erro);
    
    let mensagemDetalhe = '';
    if (erro.error && erro.error.message) {
      mensagemDetalhe = `: ${erro.error.message}`;
    } else if (typeof erro === 'string') {
      mensagemDetalhe = `: ${erro}`;
    } else if (erro.message) {
      mensagemDetalhe = `: ${erro.message}`;
    }
    
    return { mensagemErro: `${mensagem}${mensagemDetalhe}` };
  }

  mostrarMensagem(mensagem: string): void {
    this.snackBar.open(mensagem, 'Fechar', {
      duration: 5000
    });
  }
}