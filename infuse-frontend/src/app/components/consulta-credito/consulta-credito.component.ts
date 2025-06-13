import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MaterialModule } from '../../shared/material.module';
import { Subject, takeUntil } from 'rxjs';
import { ConsultaHandlerService } from '../../services/consulta-handler.service';
import { Credito } from '../../models/credito.model';

@Component({
  selector: 'app-consulta-credito',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MaterialModule
  ],
  templateUrl: './consulta-credito.component.html',
  styleUrls: ['./consulta-credito.component.scss']
})
export class ConsultaCreditoComponent implements OnInit, OnDestroy {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private consultaHandler = inject(ConsultaHandlerService);
  private destroy$ = new Subject<void>();
  
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

  ngOnInit(): void {
    this.consultaForm.get('tipoConsulta')?.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.consultaForm.get('numeroConsulta')?.setValue('');
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get tipoConsulta() {
    return this.consultaForm.get('tipoConsulta')?.value || 'nfse';
  }

  get labelConsulta() {
    return this.tipoConsulta === 'nfse' ? 'Número da NFS-e' : 'Número do Crédito';
  }

  consultar() {
    if (this.consultaForm.invalid) {
      this.consultaHandler.mostrarMensagem('Por favor, preencha o campo corretamente');
      return;
    }

    const numeroConsulta = this.consultaForm.get('numeroConsulta')?.value ?? '';
    this.resetarEstado();
    this.loading = true;
    this.tipoResultadoExibido = this.tipoConsulta as 'nfse' | 'credito';

    if (this.tipoConsulta === 'nfse') {
      this.executarConsultaPorNfse(numeroConsulta);
    } else {
      this.executarConsultaPorNumeroCredito(numeroConsulta);
    }
  }

  resetarEstado(): void {
    this.credito = null;
    this.creditos = [];
    this.creditoSelecionadoId = null;
    this.creditoDetalhe = null;
  }

  executarConsultaPorNfse(numeroNfse: string): void {
    this.consultaHandler.consultarPorNfse(numeroNfse)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          this.loading = false;
          const resultado = this.consultaHandler.processarResposta(
            response, 
            'Nenhum crédito encontrado'
          );
          if (resultado) {
            this.creditos = resultado;
          }
        },
        error: (error) => {
          this.loading = false;
          this.consultaHandler.tratarErro(error, 'Erro ao consultar créditos');
        }
      });
  }

  executarConsultaPorNumeroCredito(numeroCredito: string): void {
    this.consultaHandler.consultarPorNumeroCredito(numeroCredito)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          this.loading = false;
          const resultado = this.consultaHandler.processarResposta(
            response, 
            'Crédito não encontrado'
          );
          if (resultado) {
            this.credito = resultado;
          }
        },
        error: (error) => {
          this.loading = false;
          this.consultaHandler.tratarErro(error, 'Erro ao consultar crédito');
        }
      });
  }

  verDetalhes(numeroCredito: string): void {
    if (this.creditoSelecionadoId === numeroCredito) {
      this.creditoSelecionadoId = null;
      this.creditoDetalhe = null;
      return;
    }

    this.creditoSelecionadoId = numeroCredito;
    this.loadingDetalhe = true;
    
    this.consultaHandler.consultarPorNumeroCredito(numeroCredito)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          this.loadingDetalhe = false;
          const resultado = this.consultaHandler.processarResposta(
            response, 
            'Não foi possível carregar os detalhes do crédito'
          );
          if (resultado) {
            this.creditoDetalhe = resultado;
          }
        },
        error: (error) => {
          this.loadingDetalhe = false;
          this.creditoSelecionadoId = null;
          this.consultaHandler.tratarErro(error, 'Erro ao carregar os detalhes do crédito');
        }
      });
  }

  isCreditoSelecionado(numeroCredito: string): boolean {
    return this.creditoSelecionadoId === numeroCredito;
  }
}