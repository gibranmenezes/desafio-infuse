import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';
import { CreditoService } from '../../services/credito.service';
import { Credito } from '../../models/credito.model';

@Component({
  selector: 'app-consulta-credito',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDividerModule
  ],
  templateUrl: './consulta-credito.component.html',
  styleUrls: ['./consulta-credito.component.scss']
})
export class ConsultaCreditoComponent {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);
  private creditoService = inject(CreditoService);
  
  consultaForm = this.fb.group({
    tipoConsulta: ['nfse', Validators.required],
    numeroConsulta: ['', [Validators.required, Validators.minLength(4)]]
  });
  
  creditos: Credito[] = [];
  credito: Credito | null = null;
  creditoSelecionadoId: string | null = null;
  creditoDetalhe: Credito | null = null;
  loadingDetalhe = false;
  loading = false;
  displayedColumns: string[] = [
    'numeroCredito', 'numeroNfse', 'dataConstituicao', 
    'valorIssqn', 'tipoCredito', 'detalhes'
  ];
  
  tipoResultadoExibido: 'nfse' | 'credito' | null = null;

  constructor() {
    this.consultaForm.get('tipoConsulta')?.valueChanges.subscribe(() => {
      this.consultaForm.get('numeroConsulta')?.setValue('');
    });
  }

  get tipoConsulta() {
    return this.consultaForm.get('tipoConsulta')?.value || 'nfse';
  }

  get labelConsulta() {
    return this.tipoConsulta === 'nfse' ? 'Número da NFS-e' : 'Número do Crédito';
  }

  consultar() {
    if (this.consultaForm.invalid) {
      this.snackBar.open('Por favor, preencha o campo corretamente', 'Fechar', {
        duration: 3000
      });
      return;
    }

    const numeroConsulta = this.consultaForm.get('numeroConsulta')?.value ?? '';
    this.loading = true;
    this.credito = null;
    this.creditos = [];
    this.creditoSelecionadoId = null;
    this.creditoDetalhe = null;
    this.tipoResultadoExibido = this.tipoConsulta as 'nfse' | 'credito';

    if (this.tipoConsulta === 'nfse') {
      this.consultarPorNfse(numeroConsulta);
    } else {
      this.consultarPorNumeroCredito(numeroConsulta);
    }
  }

  consultarPorNfse(numeroNfse: string) {
    this.creditoService.getCreditosByNfse(numeroNfse).subscribe({
      next: (response) => {
        this.loading = false;
        
        if (!response) {
          this.creditos = [];
          this.snackBar.open('Nenhum dado recebido do servidor', 'Fechar', {
            duration: 3000
          });
          return;
        }
        
        if (response.status === 204 || !response.content || response.content.length === 0) {
          this.creditos = [];
          this.snackBar.open('Nenhum crédito encontrado', 'Fechar', {
            duration: 3000
          });
        } else {
          this.creditos = response.content || [];
        }
      },
      error: (error) => {
        this.loading = false;
        this.snackBar.open('Erro ao consultar créditos', 'Fechar', {
          duration: 3000
        });
        console.error('Erro ao consultar créditos:', error);
      }
    });
  }

  consultarPorNumeroCredito(numeroCredito: string) {
    this.creditoService.getCreditoByNumero(numeroCredito).subscribe({
      next: (response) => {
        this.loading = false;
        
        if (!response) {
          this.credito = null;
          this.snackBar.open('Nenhum dado recebido do servidor', 'Fechar', {
            duration: 3000
          });
          return;
        }
        
        if (response.status === 204 || !response.content) {
          this.credito = null;
          this.snackBar.open('Crédito não encontrado', 'Fechar', {
            duration: 3000
          });
        } else {
          this.credito = response.content;
        }
      },
      error: (error) => {
        this.loading = false;
        this.snackBar.open('Erro ao consultar crédito', 'Fechar', {
          duration: 3000
        });
        console.error('Erro ao consultar crédito:', error);
      }
    });
  }

  verDetalhes(numeroCredito: string) {
    if (this.creditoSelecionadoId === numeroCredito) {
      this.creditoSelecionadoId = null;
      this.creditoDetalhe = null;
      return;
    }

    this.creditoSelecionadoId = numeroCredito;
    this.loadingDetalhe = true;
    
    this.creditoService.getCreditoByNumero(numeroCredito).subscribe({
      next: (response) => {
        this.loadingDetalhe = false;
        
        if (!response || response.status === 204 || !response.content) {
          this.creditoDetalhe = null;
          this.snackBar.open('Não foi possível carregar os detalhes do crédito', 'Fechar', {
            duration: 3000
          });
        } else {
          this.creditoDetalhe = response.content;
        }
      },
      error: (error) => {
        this.loadingDetalhe = false;
        this.creditoSelecionadoId = null;
        this.snackBar.open('Erro ao carregar os detalhes do crédito', 'Fechar', {
          duration: 3000
        });
        console.error('Erro ao carregar detalhes do crédito:', error);
      }
    });
  }

  isCreditoSelecionado(numeroCredito: string): boolean {
    return this.creditoSelecionadoId === numeroCredito;
  }
}