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
  loading = false;
  displayedColumns: string[] = [
    'numeroCredito', 'numeroNfse', 'dataConstituicao', 
    'valorIssqn', 'tipoCredito', 'acoes'
  ];

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
    this.router.navigate(['/detalhe', numeroCredito]);
  }
}